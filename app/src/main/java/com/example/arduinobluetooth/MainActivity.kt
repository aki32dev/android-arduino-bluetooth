package com.example.arduinobluetooth

import android.app.Dialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.*
import android.widget.Toast
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.arduinobluetooth.adapter.PagerAdapter
import com.example.arduinobluetooth.adapter.RecyclerViewPairedAdapter
import com.example.arduinobluetooth.data.DataVar
import com.example.arduinobluetooth.databinding.ActivityMainBinding
import com.example.arduinobluetooth.model.MainViewModel
import com.example.arduinobluetooth.utility.BluetoothUtility
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {
    lateinit var bluetoothUtility   : BluetoothUtility

    private val bluetoothAdapter : BluetoothAdapter by lazy {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private lateinit var binding        : ActivityMainBinding
    private lateinit var mainViewModel  : MainViewModel

    private var term                    : Boolean               = true
    private var connectedDevice         : String                = ""
    private lateinit var dialog         : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dialog = Dialog(this)
        getSupportActionBar()!!.setSubtitle("Not Connected")
        showTab()
        bluetoothUtility = BluetoothUtility(this, handlerBluetooth)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        subscribe()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_bluetooth -> {
                bluetoothDialog()
                true
            }
            R.id.menu_setting -> {
                mainViewModel.setData("HALLO")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showTab(){
        supportActionBar?.elevation = 0f
        val pagerAdapter = PagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = resources.getString(DataVar.TAB_TITLES[position])
            tab.setIcon(DataVar.TAB_ICONS[position])
            tab.icon?.colorFilter =
                BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                    Color.WHITE,
                    BlendModeCompat.SRC_ATOP
                )
        }.attach()
    }

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

        if(pairedDevices.size > 0){
            for (device in pairedDevices) {
                dataName.add(device.name)
                dataMac.add(device.address)
            }
        }

        rvPaired.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        rvPaired.layoutManager = layoutManager
        val adapter = RecyclerViewPairedAdapter(handlerBluetooth, dataName, dataMac);
        rvPaired.adapter = adapter
    }

    private fun subscribe(){
        val dataCount = Observer<String?> { aString ->
            supportActionBar!!.subtitle = aString
        }
        mainViewModel.getData().observe(this, dataCount)
    }

    val handlerBluetooth = object:  Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when(msg.what){
                DataVar.messageStateChanged     -> when(msg.arg1){
                    DataVar.stateNone       -> {
                        term = true
                        getSupportActionBar()!!.setSubtitle("Not Connected")
                    }
                    DataVar.stateListen     -> {
                        term = false
                        getSupportActionBar()!!.setSubtitle("Not Connected")
                    }
                    DataVar.stateConnecting -> {
                        term = false
                        getSupportActionBar()!!.setSubtitle("Connecting...")
                    }
                    DataVar.stateConnected  -> {
                        term = false
                        dialog.dismiss()
                        getSupportActionBar()!!.setSubtitle("Connected with " + connectedDevice)
                    }
                }
                DataVar.messageWrite            -> {
//                    var buffer1 : ByteArray? = msg.obj as ByteArray
                }
                DataVar.messageRead             -> {
//                    val buffer = msg.obj as ByteArray
//                    var inputBuffer = String(buffer, 0, msg.arg1)
                }
                DataVar.messageDeviceName       -> {
                    connectedDevice = msg.data.getString(DataVar.deviceName)!!
                }
                DataVar.messageToast            -> {
                    val msgToast = msg.data.getString(DataVar.toast)
                    Toast.makeText(this@MainActivity, msgToast, Toast.LENGTH_SHORT).show()
                }
                DataVar.messageConnect            -> {
                    val name = msg.data.getString(DataVar.deviceName)
                    val mac = msg.data.getString(DataVar.deviceMac)
                    Toast.makeText(this@MainActivity, "Connecting to " + name, Toast.LENGTH_SHORT).show()
                    bluetoothUtility.connect(bluetoothAdapter.getRemoteDevice(mac))
                }
            }
        }
    }

}