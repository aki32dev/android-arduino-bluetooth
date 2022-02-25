package com.example.arduinobluetooth.utility;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.example.arduinobluetooth.data.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothUtility {
    private final Handler handler;
    private final BluetoothAdapter bluetoothAdapter;

    private ConnectThread       connectThread;
    private AcceptThread        acceptThread;
    private ConnectedThread     connectedThread;

    private int                 state;
    private boolean             term                = false;
    public boolean              isConnect           = false;

    private final UUID          appUUID             = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public BluetoothUtility(Handler handler){
        this.handler        = handler;
        state               = Constants.stateNone;
        bluetoothAdapter    = BluetoothAdapter.getDefaultAdapter();
    }

    public synchronized void setState(int state) {
        this.state = state;
        handler.obtainMessage(Constants.messageStateChanged, state, -1).sendToTarget();
    }

    private synchronized void start() {
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        if (acceptThread == null) {
            acceptThread = new AcceptThread();
            acceptThread.start();
        }

        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        setState(Constants.stateListen);
    }

    public synchronized void stop() {
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }
        if (acceptThread != null) {
            acceptThread.cancel();
            acceptThread = null;
        }

        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        isConnect = false;
        setState(Constants.stateNone);
    }

    public void connect(BluetoothDevice device) {
        if (state == Constants.stateConnecting) {
            connectThread.cancel();
            connectThread = null;
        }

        connectThread   = new ConnectThread(device);
        connectThread.start();
        term            = true;

        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        setState(Constants.stateConnecting);
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket serverSocket;
        @SuppressLint("MissingPermission")
        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            try {
                tmp             = bluetoothAdapter.listenUsingRfcommWithServiceRecord(Constants.appName, appUUID);
            } catch (IOException e) {
                //Log.e("Accept->Constructor", e.toString());
            }

            serverSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                //Log.e("Accept->Run", e.toString());
                try {
                    serverSocket.close();
                } catch (IOException e1) {
                    //Log.e("Accept->Close", e.toString());
                }
            }

            if (socket != null) {
                switch (state) {
                    case Constants.stateListen:
                    case Constants.stateConnecting:
                        connected(socket, socket.getRemoteDevice());
                        break;
                    case Constants.stateNone:
                    case Constants.stateConnected:
                        try {
                            socket.close();
                        } catch (IOException e) {
                            //Log.e("Accept->CloseSocket", e.toString());
                        }
                        break;
                }
            }
        }

        public void cancel() {
            try {
                serverSocket.close();
            } catch (IOException e) {
                //Log.e("Accept->CloseServer", e.toString());
            }
        }
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket socket;
        private final BluetoothDevice device;

        @SuppressLint("MissingPermission")
        public ConnectThread(BluetoothDevice device) {
            this.device = device;
            BluetoothSocket tmp = null;
            try {
                tmp = device.createRfcommSocketToServiceRecord(appUUID);
            } catch (IOException e) {
                //Log.e("Connect->Constructor", e.toString());
            }

            socket = tmp;
        }

        @SuppressLint("MissingPermission")
        public void run() {
            try {
                socket.connect();
            } catch (IOException e) {
                //Log.e("Connect->Run", e.toString());
                try {
                    socket.close();
                } catch (IOException e1) {
                    //Log.e("Connect->CloseSocket", e.toString());
                }
                connectionFailed();
                return;
            }

            synchronized (BluetoothUtility.this) {
                connectThread = null;
            }

            connected(socket, device);
        }

        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                //Log.e("Connect->Cancel", e.toString());
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket   socket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public ConnectedThread(BluetoothSocket socket) {
            this.socket         = socket;
            InputStream tmpIn   = null;
            OutputStream tmpOut = null;

            isConnect           = true;

            try {
                tmpIn   = socket.getInputStream();
                tmpOut  = socket.getOutputStream();
            } catch (IOException ignored) { }

            inputStream     = tmpIn;
            outputStream    = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (term){
                try {
                    bytes = inputStream.read(buffer);
                    handler.obtainMessage(Constants.messageRead, bytes, -1, buffer).sendToTarget();
                } catch (IOException e) {
                    connectionLost();
                }
                try{
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write(byte[] buffer) {
            try {
                outputStream.write(buffer);
                handler.obtainMessage(Constants.messageWrite, -1, -1, buffer).sendToTarget();
            } catch (IOException ignored) { }
        }

        public void cancel() {
            try {
                socket.close();
            } catch (IOException ignored) { }
        }
    }

    private void connectionLost() {
        Message message     = handler.obtainMessage(Constants.messageToast);
        Bundle bundle       = new Bundle();
        bundle.putString(Constants.messageString, "Disconnect");
        message.setData(bundle);
        handler.sendMessage(message);
        term                = false;
        isConnect           = false;

        BluetoothUtility.this.start();
    }

    private synchronized void connectionFailed() {
        Message message     = handler.obtainMessage(Constants.messageToast);
        Bundle bundle       = new Bundle();
        bundle.putString(Constants.messageString, "Unable to connect with device");
        message.setData(bundle);
        handler.sendMessage(message);
        term                = false;
        isConnect           = false;

        BluetoothUtility.this.start();
    }

    @SuppressLint("MissingPermission")
    private synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        connectedThread     = new ConnectedThread(socket);
        connectedThread.start();

        Message message     = handler.obtainMessage(Constants.messageDeviceName);
        Bundle bundle       = new Bundle();
        bundle.putString(Constants.messageString, device.getName());
        message.setData(bundle);
        handler.sendMessage(message);

        setState(Constants.stateConnected);
    }

    public void write(byte[] buffer) {
        ConnectedThread connThread;

        synchronized (this) {
            if (state != Constants.stateConnected) {
                return;
            }
            connThread = connectedThread;
        }

        connThread.write(buffer);
    }

}
