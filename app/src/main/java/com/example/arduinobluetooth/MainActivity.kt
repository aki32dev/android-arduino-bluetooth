package com.example.arduinobluetooth

import android.app.Dialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.arduinobluetooth.adapter.PagerAdapter
import com.example.arduinobluetooth.data.DataVar
import com.example.arduinobluetooth.databinding.ActivityMainBinding
import com.example.arduinobluetooth.model.MainViewModel
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {
    private val bluetoothAdapter : BluetoothAdapter by lazy {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private lateinit var binding    : ActivityMainBinding
    private lateinit var mainViewModel : MainViewModel

    val messageStateChanged         : Int                   = 0
    val messageRead                 : Int                   = 1
    val messageWrite                : Int                   = 2
    val messageDeviceName           : Int                   = 3
    val messageToast                : Int                   = 4

    var deviceName                  : String                = "deviceName"
    var toast                       : String                = "toast"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSubtitle("Not Connected")
        showTab()

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
                mainViewModel.getCurrentCount()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setSubtitle(message : CharSequence){
        supportActionBar!!.setSubtitle(message)
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
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet)

        val rvPaired = dialog.findViewById<RecyclerView>(R.id.rvPaired)
//        val pairedDevices : Set<BluetoothDevice> = bluetoothAdapter.bondedDevices
//
//        if(pairedDevices.size > 0){
//            for (device in pairedDevices) {
//                //MODEL
//            }
//        }
//
//        rvPaired.setHasFixedSize(true)
//        val layoutManager = LinearLayoutManager(this)
//        rvPaired.layoutManager = layoutManager
//        val adapter =
//        rvPaired.adapter = adapter

        //HERE

        dialog.show()
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)
    }

    private fun subscribe(){
        val dataCount = Observer<Int?> { aInt ->
            supportActionBar!!.subtitle = aInt.toString()
        }
        mainViewModel.getInitialCount().observe(this, dataCount)
    }

}