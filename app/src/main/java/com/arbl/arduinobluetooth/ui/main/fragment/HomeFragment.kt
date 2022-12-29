package com.arbl.arduinobluetooth.ui.main.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.arbl.arduinobluetooth.R
import com.arbl.arduinobluetooth.core.base.fragment.BaseFragment
import com.arbl.arduinobluetooth.ui.adapter.MessageAdapter
import com.arbl.arduinobluetooth.databinding.FragmentHomeBinding
import com.arbl.arduinobluetooth.ui.main.SharedMainViewModel
import com.arbl.arduinobluetooth.core.domain.model.MessageModel

class HomeFragment : BaseFragment() {
    private lateinit var sharedMainViewModel: SharedMainViewModel
    private lateinit var binding : FragmentHomeBinding

    private val messagesList = ArrayList<MessageModel>()
    private var stateConnect : Boolean = false

    override fun setLayout(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initObserver() {
        sharedMainViewModel = ViewModelProvider(requireActivity())[SharedMainViewModel::class.java]
        sharedMainViewModel.stateConnect.observe(viewLifecycleOwner) { state ->
            stateConnect = state
        }

        sharedMainViewModel.getReceiveData().observe(viewLifecycleOwner) { data ->
            receiveData(data)
        }
    }

    override fun initRecyclerView() {
        val messageAdapter = MessageAdapter(requireActivity(), messagesList)
        with(binding){
            rvMessage.adapter = messageAdapter
            rvMessage.scrollToPosition(messageAdapter.itemCount - 1)
        }
    }

    override fun initClickListener() {
        with(binding) {
            btnSend.setOnClickListener {
                val dataMessage = binding.etMessage.text.toString()
                if(dataMessage.isNotEmpty()) {
                    if (stateConnect){
                        binding.etMessage.text!!.clear()
                        sendData(dataMessage)
                        sharedMainViewModel.setSendData(dataMessage)
                    }
                } else{ binding.etMessage.error = getString(R.string.stringDataNotValid) }
            }

            btnDelete.setOnClickListener {
                messagesList.clear()
                initRecyclerView()
            }
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