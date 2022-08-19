package com.mp.arduinobluetooth.ui.main.dialog

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mp.arduinobluetooth.core.data.source.local.entity.Command
import com.mp.arduinobluetooth.core.domain.model.ListModel
import com.mp.arduinobluetooth.databinding.BottomSheetListBinding
import com.mp.arduinobluetooth.ui.main.SharedMainViewModel

class DialogList(
    private val parAdd: Boolean,
    private val parList: ListModel
) : BottomSheetDialogFragment(){
    private var _binding : BottomSheetListBinding?  = null
    private val binding get() = _binding!!
    private lateinit var sharedMainViewModel : SharedMainViewModel

    companion object {
        const val TAG = "DialogList"
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
        sharedMainViewModel = ViewModelProvider(requireActivity())[SharedMainViewModel::class.java]
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
            etData.setText(parList.command)
        }
    }

    private fun initClickListener(){
        with(binding) {
            btnAdd.setOnClickListener {
                val title = etTitle.text.toString().trim()
                val command = etData.text.toString().trim()
                when {
                    TextUtils.isEmpty(title) -> etTitle.error = "Please fill in the title"
                    TextUtils.isEmpty(command) -> etData.error = "Please fill in the command"
                    else -> {
                        if (parAdd) {
                            sharedMainViewModel.addCommand(
                                Command(
                                    id = parList.id,
                                    title = title,
                                    command = command
                                )
                            )
                            Toast.makeText(requireActivity(), "Add command successfully", Toast.LENGTH_SHORT).show()
                            dismiss()
                        } else {
                            sharedMainViewModel.updateCommand(
                                Command(
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
                    sharedMainViewModel.deleteCommand(
                        Command(
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