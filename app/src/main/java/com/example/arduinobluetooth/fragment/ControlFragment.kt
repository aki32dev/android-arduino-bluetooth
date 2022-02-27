package com.example.arduinobluetooth.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.arduinobluetooth.databinding.FragmentControlBinding
import com.example.arduinobluetooth.model.MainViewModel

class ControlFragment : Fragment() {
    private lateinit var mainViewModel  : MainViewModel
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
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        binding.btControlDown.setOnClickListener    { mainViewModel.setSendData("D") }
        binding.btControlLeft.setOnClickListener    { mainViewModel.setSendData("L") }
        binding.btControlUp.setOnClickListener      { mainViewModel.setSendData("U") }
        binding.btControlRight.setOnClickListener   { mainViewModel.setSendData("R") }

        binding.btControlOne.setOnClickListener     { mainViewModel.setSendData("1") }
        binding.btControlTwo.setOnClickListener     { mainViewModel.setSendData("2") }
        binding.btControlThree.setOnClickListener   { mainViewModel.setSendData("3") }
        binding.btControlFour.setOnClickListener    { mainViewModel.setSendData("4") }
    }
}