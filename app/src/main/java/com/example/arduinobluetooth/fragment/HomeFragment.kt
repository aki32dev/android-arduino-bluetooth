package com.example.arduinobluetooth.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.arduinobluetooth.databinding.FragmentHomeBinding
import com.example.arduinobluetooth.model.MainViewModel

class HomeFragment : Fragment() {
    private lateinit var mainViewModel: MainViewModel

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        subscribe()

    }

    private fun subscribe(){
        val dataCount = Observer<Int?> { aInt ->
            binding.tvFrag1.text = aInt.toString()
        }
        mainViewModel.getInitialCount().observe(viewLifecycleOwner, dataCount)
    }
}