package com.example.arduinobluetooth.utility

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.example.arduinobluetooth.data.DataVar
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

@SuppressLint("ServiceCast")
class BluetoothUtility(private val context : Context, private val handler : Handler){
    /*===================================GLOBAL CLASS VARIABEL===================================*/
    val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    var bluetoothAdapter : BluetoothAdapter

    var statenow                    : Int               = 0
    var isConnect                   : Boolean           = false
    var term                        : Boolean           = false

    private var acceptThread        : AcceptThread?     = null
    private var connectThread       : ConnectThread?    = null
    private var connectedThread     : ConnectedThread?  = null

    /*========================================CONSTRUCTOR========================================*/
    init {
        statenow = DataVar.stateNone
        bluetoothAdapter = bluetoothManager.adapter
    }

    /*=========================================FUNCTION==========================================*/
    fun getState():Int{
        return statenow
    }

    fun setState(state : Int){
        this.statenow = state
        handler.obtainMessage(DataVar.messageStateChanged, state, -1).sendToTarget()
    }

    @Synchronized
    private fun start() {
        if (connectThread != null) {
            connectThread!!.cancel()
            connectThread = null
        }
        if (acceptThread == null) {
            acceptThread = AcceptThread()
            acceptThread!!.start()
        }
        if (connectedThread != null) {
            connectedThread!!.cancel()
            connectedThread = null
        }
        setState(DataVar.stateListen)
    }

    @Synchronized
    fun stop() {
        if (connectThread != null) {
            connectThread!!.cancel()
            connectThread = null
        }
        if (acceptThread != null) {
            acceptThread!!.cancel()
            acceptThread = null
        }
        if (connectedThread != null) {
            connectedThread!!.cancel()
            connectedThread = null
        }
        setState(DataVar.stateNone)
    }

    fun connect(device: BluetoothDevice) {
        if (statenow == DataVar.stateConnecting) {
            connectThread!!.cancel()
            connectThread = null
        }
        connectThread = ConnectThread(device)
        connectThread!!.start()
        term = true
        if (connectedThread != null) {
            connectedThread!!.cancel()
            connectedThread = null
        }
        setState(DataVar.stateConnecting)
    }

    /*==========================================SUPPORT==========================================*/

    private fun connectionLost() {
        val message = handler.obtainMessage(DataVar.messageToast)
        val bundle = Bundle()
        bundle.putString(DataVar.toast, "Koneksi terputus")
        message.data = bundle
        handler.sendMessage(message)
        term = false
        isConnect = false
        start()
    }

    @Synchronized
    private fun connectionFailed() {
        val message = handler.obtainMessage(DataVar.messageToast)
        val bundle = Bundle()
        bundle.putString(DataVar.toast, "Tidak dapat tersambung dengan perangkat")
        message.data = bundle
        handler.sendMessage(message)
        term = false
        isConnect = false
        start()
    }

    @SuppressLint("MissingPermission")
    @Synchronized
    private fun connected(socket: BluetoothSocket?, device: BluetoothDevice) {
        if (connectThread != null){
            connectThread!!.cancel()
            connectThread = null
        }
        if (connectedThread != null) {
            connectedThread!!.cancel()
            connectedThread = null
        }
        connectedThread = ConnectedThread(socket)
        connectedThread!!.start()
        val message = handler.obtainMessage(DataVar.messageDeviceName)
        val bundle = Bundle()
        bundle.putString(DataVar.deviceName, device.name)
        message.data = bundle
        handler.sendMessage(message)
        setState(DataVar.stateConnected)
    }

    fun write(buffer: ByteArray?) {
        var connThread: ConnectedThread?
        synchronized(this) {
            if (statenow != DataVar.stateConnected) {
                return
            }
            connThread = connectedThread
        }
        connThread!!.write(buffer)
    }

    /*==========================================THREAD===========================================*/
    @SuppressLint("MissingPermission")
    private inner class AcceptThread : Thread(){
        private val serverSocket : BluetoothServerSocket?

        init {
            var tmp : BluetoothServerSocket? = null
            try {
                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(DataVar.appName, DataVar.appUUID)
            }catch (e : IOException){
                Log.e("Accept->Constructor", e.toString())
            }
            serverSocket = tmp
        }

        override fun run() {
            var socket : BluetoothSocket? = null
            try {
                socket = serverSocket!!.accept()
            } catch (e : IOException){
                Log.e("Accept->Run", e.toString())
                try {
                    serverSocket!!.close()
                } catch (e1: IOException) {
                    Log.e("Accept->Close", e.toString())
                }
            }
            if (socket != null) {
                when (statenow) {
                    DataVar.stateListen, DataVar.stateConnecting -> connected(socket, socket.remoteDevice)
                    DataVar.stateNone, DataVar.stateConnected -> try {
                        socket.close()
                    } catch (e: IOException) {
                        Log.e("Accept->CloseSocket", e.toString())
                    }
                }
            }
        }

        fun cancel(){
            try {
                serverSocket!!.accept()
            }catch (e : IOException){
                Log.e("Accept->CloseServer", e.toString())
            }
        }
    }

    @SuppressLint("MissingPermission")
    private inner class ConnectThread(private val device: BluetoothDevice) : Thread(){
        private val socket: BluetoothSocket?

        init {
            var tmp: BluetoothSocket? = null
            try {
                tmp = device.createRfcommSocketToServiceRecord(DataVar.appUUID)
            } catch (e: IOException) {
                Log.e("Connect->Constructor", e.toString())
            }
            socket = tmp
        }

        @SuppressLint("MissingPermission")
        override fun run() {
            try {
                socket!!.connect()
            } catch (e: IOException) {
                Log.e("Connect->Run", e.toString())
                try {
                    socket!!.close()
                } catch (e1: IOException) {
                    Log.e("Connect->CloseSocket", e.toString())
                }
                connectionFailed()
                return
            }
            synchronized(this@BluetoothUtility) {
                connectThread = null
            }
            connected(socket, device)
        }

        fun cancel() {
            try {
                socket!!.close()
            } catch (e: IOException) {
                Log.e("Connect->Cancel", e.toString())
            }
        }
    }

    private inner class ConnectedThread(private val socket: BluetoothSocket?) : Thread() {
        private val inputStream: InputStream?
        private val outputStream: OutputStream?

        init {
            var tmpIn: InputStream? = null
            var tmpOut: OutputStream? = null
            isConnect = true
            try {
                tmpIn = socket!!.inputStream
                tmpOut = socket.outputStream
            } catch (e: IOException) {
            }
            inputStream = tmpIn
            outputStream = tmpOut
        }

        override fun run() {
            val buffer = ByteArray(1024)
            var bytes: Int
            while (term) {
                try {
                    bytes = inputStream!!.read(buffer)
                    handler.obtainMessage(DataVar.messageRead, bytes, -1, buffer)
                        .sendToTarget()
                } catch (e: IOException) {
                    connectionLost()
                }
                try {
                    sleep(200)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }

        fun write(buffer: ByteArray?) {
            try {
                outputStream!!.write(buffer)
                handler.obtainMessage(DataVar.messageWrite, -1, -1, buffer).sendToTarget()
            } catch (e: IOException) {
            }
        }

        fun cancel() {
            try {
                socket!!.close()
            } catch (e: IOException) {
            }
        }
    }
}