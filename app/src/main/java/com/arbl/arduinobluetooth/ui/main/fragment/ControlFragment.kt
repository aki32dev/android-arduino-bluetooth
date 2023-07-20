package com.arbl.arduinobluetooth.ui.main.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.arbl.arduinobluetooth.core.base.fragment.BaseFragment
import com.arbl.arduinobluetooth.databinding.FragmentControlBinding
import com.arbl.arduinobluetooth.ui.main.SharedMainViewModel

class ControlFragment : BaseFragment() {
    private lateinit var binding : FragmentControlBinding
    private lateinit var sharedMainViewModel: SharedMainViewModel
    private var stateConnect : Boolean = false

    override fun setLayout(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentControlBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initObserver() {
        super.initObserver()
        sharedMainViewModel = ViewModelProvider(requireActivity())[SharedMainViewModel::class.java]
        sharedMainViewModel.stateConnect.observe(viewLifecycleOwner) { state ->
            stateConnect = state
        }
    }

    override fun initClickListener() {
        super.initClickListener()
        with(binding) {

        }
    }
}