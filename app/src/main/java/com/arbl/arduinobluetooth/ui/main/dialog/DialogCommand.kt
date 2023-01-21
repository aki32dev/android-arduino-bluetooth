package com.arbl.arduinobluetooth.ui.main.dialog

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.arbl.arduinobluetooth.core.domain.model.CommandModel
import com.arbl.arduinobluetooth.core.domain.model.ListModel
import com.arbl.arduinobluetooth.databinding.BottomSheetListBinding
import com.arbl.arduinobluetooth.ui.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DialogCommand(
    private val parAdd: Boolean,
    private val parList: ListModel
) : BottomSheetDialogFragment(){
    private var _binding : BottomSheetListBinding?  = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by viewModel()

    companion object {
        const val TAG = "DialogCommand"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initClickListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initView() {
        with(binding) {
            etTitle.setText(parList.title)
            etCommand.setText(parList.command)
        }
    }

    private fun initClickListener(){
        with(binding) {
            btnAdd.setOnClickListener {
                val title = etTitle.text.toString().trim()
                val command = etCommand.text.toString().trim()
                when {
                    TextUtils.isEmpty(title) -> etTitle.error = "Please fill in the title"
                    TextUtils.isEmpty(command) -> etCommand.error = "Please fill in the command"
                    else -> {
                        if (parAdd) {
                            mainViewModel.insertCommand(
                                CommandModel(
                                    id = parList.id,
                                    title = title,
                                    command = command
                                )
                            )
                            Toast.makeText(requireActivity(), "Add command successfully", Toast.LENGTH_SHORT).show()
                            dismiss()
                        } else {
                            mainViewModel.updateCommand(
                                CommandModel(
                                    id = parList.id,
                                    title = title,
                                    command = command
                                )
                            )
                            Toast.makeText(requireActivity(), "Update command successfully", Toast.LENGTH_SHORT).show()
                            dismiss()
                        }
                    }
                }
            }

            btnDelete.setOnClickListener {
                if (parList.id != 0) {
                    mainViewModel.deleteCommand(
                        CommandModel(
                            id = parList.id,
                            title = parList.title,
                            command = parList.command
                        )
                    )
                    Toast.makeText(requireActivity(), "Delete command successfully", Toast.LENGTH_SHORT).show()
                    dismiss()
                } else {
                    Toast.makeText(requireActivity(), "No command", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}