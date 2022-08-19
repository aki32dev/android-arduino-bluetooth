package com.mp.arduinobluetooth.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mp.arduinobluetooth.R
import com.mp.arduinobluetooth.adapter.MessageAdapter
import com.mp.arduinobluetooth.databinding.FragmentHomeBinding
import com.mp.arduinobluetooth.ui.main.SharedMainViewModel
import com.mp.arduinobluetooth.core.domain.model.MessageModel


class HomeFragment : Fragment() {
    private lateinit var sharedMainViewModel: SharedMainViewModel
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val messagesList = ArrayList<MessageModel>()
    private var stateConnect : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedMainViewModel = ViewModelProvider(requireActivity())[SharedMainViewModel::class.java]
        initRecyclerView()
        initClickListener()
        initObserver()

    }

    private fun initClickListener() {
        with(binding) {
            btnSend.setOnClickListener {
                val dataMessage = binding.edMessage.text.toString()
                if(dataMessage.isNotEmpty()) {
                    if (stateConnect){
                        binding.edMessage.text!!.clear()
                        sendData(dataMessage)
                        sharedMainViewModel.setSendData(dataMessage)
                    }
                } else{ binding.edMessage.error = getString(R.string.stringDataNotValid) }
            }

            btnDelete.setOnClickListener {
                messagesList.clear()
                initRecyclerView()
            }
        }
    }

    private fun initObserver() {
        sharedMainViewModel.stateConnect.observe(viewLifecycleOwner) { state ->
            stateConnect = state
        }

        sharedMainViewModel.getReceiveData().observe(viewLifecycleOwner) { data ->
            receiveData(data)
        }
    }

    private fun initRecyclerView() {
        val messageAdapter = MessageAdapter(requireActivity(), messagesList)
        with(binding){
            rvMessage.adapter = messageAdapter
            rvMessage.scrollToPosition(messageAdapter.itemCount - 1)
        }
    }

    private fun sendData(data : String){
        messagesList.add(MessageModel(data, MessageAdapter.MESSAGE_TYPE_OUT))
        initRecyclerView()
    }

    private fun receiveData(data : String){
        messagesList.add(MessageModel(data, MessageAdapter.MESSAGE_TYPE_IN))
        initRecyclerView()
    }
}