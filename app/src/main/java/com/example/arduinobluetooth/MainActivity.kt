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
import androidx.recyclerview.widget.RecyclerView
import com.example.arduinobluetooth.adapter.PagerAdapter
import com.example.arduinobluetooth.data.DataVar
import com.example.arduinobluetooth.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    private val bluetoothAdapter : BluetoothAdapter by lazy {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSubtitle("Not Connected")
        showTab()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_bluetooth -> {
                showDialog()
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
        }.attach()
    }

    private fun showDialog(){
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
}