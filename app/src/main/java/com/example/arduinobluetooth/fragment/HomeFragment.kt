package com.example.arduinobluetooth.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arduinobluetooth.R
import com.example.arduinobluetooth.adapter.RecyclerViewMessageAdapter
import com.example.arduinobluetooth.databinding.FragmentHomeBinding
import com.example.arduinobluetooth.model.SharedViewModel
import com.example.arduinobluetooth.model.MessageModel
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {
    private lateinit var sharedViewModel: SharedViewModel
    private var _binding                : FragmentHomeBinding?      = null
    private val binding get()                                       = _binding!!

    private val messagesList            : ArrayList<MessageModel>   = ArrayList()
    private var stateConnect            : Boolean                   = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        liveState()
        liveReceive()

        with(binding){
            btSendMessage.setOnClickListener {
                val dataMessage = binding.edMessage.text.toString()
                if(dataMessage.isNotEmpty()) {
                    if (stateConnect){
                        binding.edMessage.text!!.clear()
                        sendData(dataMessage)
                        sharedViewModel.setSendData(dataMessage)
                    }
                } else{ binding.edMessage.error = getString(R.string.stringDataNotValid) }
            }

            btDeleteMessage.setOnClickListener {
                messagesList.clear()
                updateRv()
            }
        }
    }

    private fun updateRv(){
        val adapter = RecyclerViewMessageAdapter(requireActivity(), messagesList)
        with(binding){
            rvMessage.layoutManager = LinearLayoutManager(context)
            rvMessage.adapter       = adapter
            rvMessage.scrollToPosition(adapter.itemCount - 1)
        }
    }

    private fun sendData(data : String){
        messagesList.add(MessageModel(data, RecyclerViewMessageAdapter.MESSAGE_TYPE_OUT))
        updateRv()
    }

    private fun receiveData(data : String){
        messagesList.add(MessageModel(data, RecyclerViewMessageAdapter.MESSAGE_TYPE_IN))
        updateRv()
    }

    private fun liveState(){
        val liveObs = Observer<Boolean?> { aBoolean ->
            stateConnect = aBoolean
        }
        sharedViewModel.getState().observe(viewLifecycleOwner, liveObs)
    }

    private fun liveReceive(){
        val liveObs = Observer<String?> { aString ->
            receiveData(aString)
        }
        sharedViewModel.getReceiveData().observe(viewLifecycleOwner, liveObs)
    }
}