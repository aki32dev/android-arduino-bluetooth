package com.example.arduinobluetooth.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.arduinobluetooth.fragment.ControlFragment
import com.example.arduinobluetooth.fragment.HomeFragment
import com.example.arduinobluetooth.fragment.ListFragment

class PagerAdapter(activity : AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int { return 3 }

    override fun createFragment(position: Int): Fragment {
        var fragment : Fragment? = null
        when(position) {
            0 -> fragment = HomeFragment()
            1 -> fragment = ControlFragment()
            2 -> fragment = ListFragment()
        }
        return fragment as Fragment
    }

}