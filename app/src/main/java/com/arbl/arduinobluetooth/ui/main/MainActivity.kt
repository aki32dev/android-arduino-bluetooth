package com.arbl.arduinobluetooth.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import android.provider.Settings.ACTION_BLUETOOTH_SETTINGS
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.arbl.arduinobluetooth.ui.adapter.PagerAdapter
import com.arbl.arduinobluetooth.utils.constant.Constants
import com.arbl.arduinobluetooth.databinding.ActivityMainBinding
import com.arbl.arduinobluetooth.utils.bluetooth.BluetoothUtils
import com.google.android.material.tabs.TabLayoutMediator
import com.arbl.arduinobluetooth.R
import com.arbl.arduinobluetooth.core.domain.model.ListModel
import com.arbl.arduinobluetooth.core.domain.model.PairedModel
import com.arbl.arduinobluetooth.ui.main.dialog.DialogCommand
import com.arbl.arduinobluetooth.ui.main.dialog.DialogPaired
import com.arbl.arduinobluetooth.utils.constant.Constants.TAB_TITLES
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedMainViewModel: SharedMainViewModel
    private lateinit var bluetoothUtils : BluetoothUtils
    private val bluetoothAdapter : BluetoothAdapter by lazy {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }
    private var dataString : String = ""
    private var connectedDevice : String = ""
    private var backPressedTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        sharedMainViewModel = ViewModelProvider(this)[SharedMainViewModel::class.java]
        bluetoothUtils = BluetoothUtils(handlerBluetooth)
        showTab()
        initClickListener()
        initObserver()
    }

    override fun onRequestPermissionsResult(
        requestCode : Int,
        permissions : Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == Constants.bluetoothRequestPermit){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(bluetoothUtils.isConnect){
                    bluetoothUtils.stop()
                } else{
                    enableBluetooth()
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
            bluetoothUtils.stop()
            finish()
        } else {
            Toast.makeText(this, "Press back again to leave the app.", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }

    override fun onDestroy() {
        super.onDestroy()
        bluetoothUtils.stop()
    }

    private fun initClickListener() {
        with(binding) {
            appBarLayout.btnBluetooth.setOnClickListener {
                enableBluetooth()
            }

            appBarLayout.btnSetting.setOnClickListener {
                startActivity(Intent(ACTION_BLUETOOTH_SETTINGS))
            }
        }
    }

    private fun initObserver() {
        sharedMainViewModel.sendData.observe(this) { data ->
            bluetoothUtils.write(data.toByteArray())
        }

        sharedMainViewModel.macData.observe(this) { mac ->
            bluetoothUtils.connect(bluetoothAdapter.getRemoteDevice(mac))
        }

        sharedMainViewModel.nameData.observe(this) { name ->
            Toast.makeText(this@MainActivity, "Connecting to $name", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showTab(){
        supportActionBar?.elevation = 0f
        val pagerAdapter = PagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    @SuppressLint("MissingPermission")
    private fun showDialogBluetooth(){
        val pairedDevices : Set<BluetoothDevice> = bluetoothAdapter.bondedDevices
        val listDevices = mutableListOf<PairedModel>()
        if(pairedDevices.isNotEmpty()){
            for (device in pairedDevices) {
                listDevices.add(
                    PairedModel(
                        name = device.name,
                        mac = device.address
                    )
                )
            }
        }

        DialogPaired(listDevices).apply {
            setStyle(
                BottomSheetDialogFragment.STYLE_NORMAL,
                R.style.BaseBottomSheetDialog
            )
            show(supportFragmentManager, DialogPaired.TAG)
        }
    }

    fun showDialogAdd(parAdd: Boolean ,listModel: ListModel) {
        DialogCommand(parAdd, listModel).apply {
            setStyle(
                BottomSheetDialogFragment.STYLE_NORMAL,
                R.style.BaseBottomSheetDialog
            )
            show(supportFragmentManager, DialogCommand.TAG)
        }
    }

    private fun isBluetoothPermissionNotGranted() : Boolean {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) && (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)
    }

    private var launchActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            enableBluetooth()
        }
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
                if (bluetoothUtils.isConnect) {
                    bluetoothUtils.stop()
                } else {
                    showDialogBluetooth()
                }
            }
        }
    }

    private val handlerBluetooth = object:  Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when(msg.what){
                Constants.messageStateChanged -> when(msg.arg1){
                    Constants.stateNone -> {
                        sharedMainViewModel.setState(false)
                        binding.appBarLayout.tvSubtitle.text = getString(R.string.stringNC)
                    }

                    Constants.stateListen -> {
                        sharedMainViewModel.setState(false)
                        binding.appBarLayout.tvSubtitle.text = getString(R.string.stringNC)
                    }

                    Constants.stateConnecting -> {
                        sharedMainViewModel.setState(false)
                        binding.appBarLayout.tvSubtitle.text = getString(R.string.stringCTI)
                    }

                    Constants.stateConnected -> {
                        sharedMainViewModel.setState(true)
                        val newText = this@MainActivity.resources.getString(R.string.stringCTD, connectedDevice)
                        binding.appBarLayout.tvSubtitle.text = newText
                    }
                }

                Constants.messageRead -> {
                    val buffer = msg.obj as ByteArray
                    val inputBuffer = String(buffer, 0, msg.arg1)
                    dataString += inputBuffer
                    CoroutineScope(Dispatchers.Default).launch {
                        delay(297)
                        if(dataString.isNotEmpty()){
                            sharedMainViewModel.setReceiveData(dataString)
                            dataString = ""
                        }
                    }
                }

                Constants.messageDeviceName -> {
                    connectedDevice = msg.data.getString(Constants.messageString)!!
                }

                Constants.messageToast -> {
                    val msgToast = msg.data.getString(Constants.messageString)
                    Toast.makeText(this@MainActivity, msgToast, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}