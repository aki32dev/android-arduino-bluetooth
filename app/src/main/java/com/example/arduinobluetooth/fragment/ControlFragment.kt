package com.example.arduinobluetooth.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.arduinobluetooth.databinding.FragmentControlBinding
import com.example.arduinobluetooth.model.MainViewModel

class ControlFragment : Fragment() {
    private lateinit var mainViewModel: MainViewModel

    private var _binding: FragmentControlBinding? = null
    private val binding get() = _binding!!

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
        subscribe()
    }

    private fun subscribe(){
        val dataCount = Observer<String?> { aString ->
            binding.tvFrag2.text = aString.toString()
        }
        mainViewModel.getData().observe(viewLifecycleOwner, dataCount)
    }
}