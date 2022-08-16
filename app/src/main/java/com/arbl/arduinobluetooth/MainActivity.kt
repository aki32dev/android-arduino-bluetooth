package com.arbl.arduinobluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.*
import android.provider.Settings.ACTION_BLUETOOTH_SETTINGS
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arbl.arduinobluetooth.adapter.PagerAdapter
import com.arbl.arduinobluetooth.adapter.RecyclerViewPairedAdapter
import com.arbl.arduinobluetooth.data.Constants
import com.arbl.arduinobluetooth.databinding.ActivityMainBinding
import com.arbl.arduinobluetooth.model.SharedViewModel
import com.arbl.arduinobluetooth.utility.BluetoothUtility
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    lateinit var bluetoothUtility       : BluetoothUtility
    private val bluetoothAdapter        : BluetoothAdapter by lazy {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private lateinit var binding        : ActivityMainBinding
    private lateinit var sharedViewModel: SharedViewModel

    private var dataString              : String                = ""
    private var connectedDevice         : String                = ""
    private lateinit var dialog         : Dialog

    private var backPressedTime         : Long                  = 0

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dialog = Dialog(this)
        setSubtitle(getString(R.string.stringNC))
        showTab()
        bluetoothUtility = BluetoothUtility(handlerBluetooth)

        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        sendSubscribe()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_bluetooth -> {
                enableBluetooth()
                true
            }
            R.id.menu_setting   -> {
                startActivity(Intent(ACTION_BLUETOOTH_SETTINGS))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode : Int,
        permissions : Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == Constants.bluetoothRequestPermit){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(bluetoothUtility.isConnect){
                    bluetoothUtility.stop()
                } else{
                    bluetoothDialog()
                }
            } else {
                Toast.makeText(this, "Bluetooth permission required on Android 12+", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            bluetoothUtility.stop()
            finish()
        } else {
            Toast.makeText(this, "Press back again to leave the app.", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }

    override fun onDestroy() {
        super.onDestroy()
        bluetoothUtility.stop()
    }

    private fun setSubtitle(title : CharSequence) {
        supportActionBar!!.subtitle = title
    }

    private fun showTab(){
        supportActionBar?.elevation = 0f
        val pagerAdapter = PagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = resources.getString(Constants.TAB_TITLES[position])
        }.attach()
    }

    private fun isBluetoothPermissionNotGranted() : Boolean {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) && (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)
    }

    private var launchActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) { enableBluetooth() }
    }

    @SuppressLint("MissingPermission")
    private fun enableBluetooth(){
        if (isBluetoothPermissionNotGranted()) {
            ActivityCompat.requestPermissions(this@MainActivity,
                arrayOf(Manifest.permission.BLUETOOTH_CONNECT), Constants.bluetoothRequestPermit)
        } else {
            if (!bluetoothAdapter.isEnabled) {
                val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                launchActivityResult.launch(enableIntent)
            } else {
                if (bluetoothUtility.isConnect) {
                    bluetoothUtility.stop()
                } else {
                    bluetoothDialog()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun bluetoothDialog(){
        dialog.setContentView(R.layout.bottom_sheet)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.show()

        val rvPaired = dialog.findViewById<RecyclerView>(R.id.rvPaired)
        val dataName = ArrayList<String>()
        val dataMac = ArrayList<String>()
        val pairedDevices : Set<BluetoothDevice> = bluetoothAdapter.bondedDevices

        if(pairedDevices.isNotEmpty()){
            for (device in pairedDevices) {
                dataName.add(device.name)
                dataMac.add(device.address)
            }
        }

        rvPaired.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        rvPaired.layoutManager = layoutManager
        val adapter = RecyclerViewPairedAdapter(handlerBluetooth, dataName, dataMac)
        rvPaired.adapter = adapter
    }

    private fun sendSubscribe(){
        val dataCount = Observer<String?> { aString ->
            bluetoothUtility.write(aString.toByteArray())
        }
        sharedViewModel.getSendData().observe(this, dataCount)
    }

    private val handlerBluetooth = object:  Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when(msg.what){
                Constants.messageStateChanged     -> when(msg.arg1){
                    Constants.stateNone       -> {
                        sharedViewModel.setState(false)
                        setSubtitle(getString(R.string.stringNC))
                    }
                    Constants.stateListen     -> {
                        sharedViewModel.setState(false)
                        setSubtitle(getString(R.string.stringNC))
                    }
                    Constants.stateConnecting -> {
                        sharedViewModel.setState(false)
                        setSubtitle(getString(R.string.stringCTI))
                    }
                    Constants.stateConnected  -> {
                        sharedViewModel.setState(true)
                        dialog.dismiss()
                        val newText = this@MainActivity.resources.getString(R.string.stringCTD, connectedDevice)
                        setSubtitle(newText)
                    }
                }
                Constants.messageWrite            -> {  }
                Constants.messageRead             -> {
                    val buffer = msg.obj as ByteArray
                    val inputBuffer = String(buffer, 0, msg.arg1)
                    dataString += inputBuffer
                    CoroutineScope(Dispatchers.Default).launch {
                        delay(300)
                        if(dataString.isNotEmpty()){
                            sharedViewModel.setReceiveData(dataString)
                            dataString = ""
                        }
                    }
                }
                Constants.messageDeviceName       -> {
                    connectedDevice = msg.data.getString(Constants.messageString)!!
                }
                Constants.messageToast            -> {
                    val msgToast = msg.data.getString(Constants.messageString)
                    Toast.makeText(this@MainActivity, msgToast, Toast.LENGTH_SHORT).show()
                }
                Constants.messageConnect          -> {
                    val name = msg.data.getString(Constants.deviceName)
                    val mac = msg.data.getString(Constants.deviceMac)
                    Toast.makeText(this@MainActivity, "Connecting to $name", Toast.LENGTH_SHORT).show()
                    bluetoothUtility.connect(bluetoothAdapter.getRemoteDevice(mac))
                }
            }
        }
    }

}