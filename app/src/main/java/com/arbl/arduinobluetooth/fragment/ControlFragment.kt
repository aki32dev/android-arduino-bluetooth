package com.arbl.arduinobluetooth.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.arbl.arduinobluetooth.databinding.FragmentControlBinding
import com.arbl.arduinobluetooth.model.SharedViewModel

class ControlFragment : Fragment() {
    private lateinit var sharedViewModel: SharedViewModel
    private var _binding                : FragmentControlBinding?   = null
    private val binding get()                                       = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentControlBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        with(binding){
            btControlDown.setOnClickListener    { sharedViewModel.setSendData("D") }
            btControlLeft.setOnClickListener    { sharedViewModel.setSendData("L") }
            btControlUp.setOnClickListener      { sharedViewModel.setSendData("U") }
            btControlRight.setOnClickListener   { sharedViewModel.setSendData("R") }

            btControlOne.setOnClickListener     { sharedViewModel.setSendData("1") }
            btControlTwo.setOnClickListener     { sharedViewModel.setSendData("2") }
            btControlThree.setOnClickListener   { sharedViewModel.setSendData("3") }
            btControlFour.setOnClickListener    { sharedViewModel.setSendData("4") }
        }
    }
}