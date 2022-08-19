package com.mp.arduinobluetooth.ui.main.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.mp.arduinobluetooth.adapter.ListAdapter
import com.mp.arduinobluetooth.core.domain.model.ListModel
import com.mp.arduinobluetooth.databinding.FragmentListBinding
import com.mp.arduinobluetooth.ui.main.MainActivity
import com.mp.arduinobluetooth.ui.main.SharedMainViewModel

class ListFragment : Fragment() {
    private lateinit var sharedMainViewModel: SharedMainViewModel
    private var _binding : FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var listAdapter: ListAdapter
    private var stateConnect : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedMainViewModel = ViewModelProvider(requireActivity())[SharedMainViewModel::class.java]
        initClickListener()
        initRecyclerView()
        initObserver()
    }

    private fun initClickListener() {
        with(binding) {
            btnAdd.setOnClickListener {
                (activity as MainActivity).showDialogAdd(true,
                    ListModel(
                        id = 0,
                        title = "",
                        command = ""
                    )
                )
            }
        }
    }

    private fun initRecyclerView() {
        listAdapter = ListAdapter(
            onEditClick = { model ->
                (activity as MainActivity).showDialogAdd(false, model)
            },
            onDeleteClick = { model ->
                (activity as MainActivity).showDialogAdd(false, model)
            },
            onSendClick = { model ->
                if (stateConnect){
                    sharedMainViewModel.setSendData(model.command)
                    Toast.makeText(requireActivity(), "Send successfully", Toast.LENGTH_SHORT).show()
                }
            }
        )

        with(binding) {
            rvData.adapter = listAdapter
        }
    }

    private fun initObserver() {
        sharedMainViewModel.stateConnect.observe(viewLifecycleOwner) { state ->
            stateConnect = state
        }

        sharedMainViewModel.readAllData.observe(viewLifecycleOwner) { data ->
            val convertList = mutableListOf<ListModel>()
            for (item in data) {
                convertList.add(
                    ListModel(
                        id = item.id,
                        title = item.title,
                        command = item.command
                    )
                )
            }
            listAdapter.submitList(convertList)
            if (listAdapter.itemCount > 0) {
                binding.tvEmpty.visibility = View.GONE
            } else {
                binding.tvEmpty.visibility = View.VISIBLE
            }
        }
    }
}