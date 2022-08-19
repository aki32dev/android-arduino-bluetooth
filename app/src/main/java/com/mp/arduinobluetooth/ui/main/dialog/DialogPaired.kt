package com.mp.arduinobluetooth.ui.main.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mp.arduinobluetooth.adapter.PairedAdapter
import com.mp.arduinobluetooth.core.domain.model.PairedModel
import com.mp.arduinobluetooth.databinding.BottomSheetPairedBinding
import com.mp.arduinobluetooth.ui.main.SharedMainViewModel

class DialogPaired(
    private val parPaired: List<PairedModel>
) : BottomSheetDialogFragment(){
    private var _binding : BottomSheetPairedBinding?  = null
    private val binding get() = _binding!!
    private lateinit var pairedAdapter : PairedAdapter
    private lateinit var sharedMainViewModel : SharedMainViewModel

    companion object {
        const val TAG = "DialogPaired"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetPairedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedMainViewModel = ViewModelProvider(requireActivity())[SharedMainViewModel::class.java]
        initRecyclerView()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initRecyclerView(){
        pairedAdapter = PairedAdapter(
            onItemClick = { device ->
                sharedMainViewModel.setNameData(device.name)
                sharedMainViewModel.setMacData(device.mac)
                dismiss()
            }
        )

        with(binding) {
            rvPaired.apply {
                adapter = pairedAdapter
            }
        }
        pairedAdapter.submitList(parPaired)
    }
}