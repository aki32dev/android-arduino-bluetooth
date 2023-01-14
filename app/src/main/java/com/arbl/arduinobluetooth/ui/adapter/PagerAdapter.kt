package com.arbl.arduinobluetooth.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.arbl.arduinobluetooth.ui.main.fragment.HomeFragment
import com.arbl.arduinobluetooth.ui.main.fragment.ListFragment

class PagerAdapter(
    activity : AppCompatActivity
) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int { return 2 }

    override fun createFragment(position: Int): Fragment {
        var fragment : Fragment? = null
        when(position) {
            0 -> fragment = HomeFragment()
            1 -> fragment = ListFragment()
        }
        return fragment as Fragment
    }

}