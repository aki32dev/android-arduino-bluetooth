package com.arbl.arduinobluetooth.ui.main.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.arbl.arduinobluetooth.R
import com.arbl.arduinobluetooth.core.base.fragment.BaseFragment
import com.arbl.arduinobluetooth.ui.adapter.ListAdapter
import com.arbl.arduinobluetooth.core.domain.model.ListModel
import com.arbl.arduinobluetooth.databinding.FragmentListBinding
import com.arbl.arduinobluetooth.ui.main.MainActivity
import com.arbl.arduinobluetooth.ui.main.MainViewModel
import com.arbl.arduinobluetooth.ui.main.SharedMainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListFragment : BaseFragment() {
    private lateinit var binding : FragmentListBinding
    private val mainViewModel: MainViewModel by viewModel()
    private lateinit var sharedMainViewModel: SharedMainViewModel
    private lateinit var listAdapter: ListAdapter
    private var stateConnect : Boolean = false

    override fun setLayout(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initObserver() {
        sharedMainViewModel = ViewModelProvider(requireActivity())[SharedMainViewModel::class.java]
        sharedMainViewModel.stateConnect.observe(viewLifecycleOwner) { state ->
            stateConnect = state
        }

        mainViewModel.commandliveData.observe(viewLifecycleOwner) { data ->
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

    override fun initRecyclerView() {
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
                    Toast.makeText(requireActivity(), getString(R.string.stringSendSuccessfully), Toast.LENGTH_SHORT).show()
                }
            }
        )
        with(binding) {
            rvData.adapter = listAdapter
        }
    }

    override fun initClickListener() {
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
}