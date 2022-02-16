package com.example.arduinobluetooth.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arduinobluetooth.R
import com.example.arduinobluetooth.adapter.RecyclerViewDataAdapter
import com.example.arduinobluetooth.data.DataVar
import com.example.arduinobluetooth.data.ItemData
import com.example.arduinobluetooth.database.LocalDB
import com.example.arduinobluetooth.databinding.FragmentListBinding
import com.example.arduinobluetooth.model.MainViewModel
import kotlin.collections.ArrayList

class ListFragment : Fragment() {
    private lateinit var mainViewModel: MainViewModel
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private var localDB : LocalDB? = null
    private val list = ArrayList<ItemData>()
    private lateinit var dialog : Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = Dialog(requireContext())
        localDB = LocalDB(requireContext())

        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        subscribe()
        list.addAll(readDB())
        showRecycler()

        binding.btAdd.setOnClickListener { addDialog("", "") }
    }

    private fun subscribe(){
        val dataCount = Observer<String?> { aString ->
            binding.tvEmpty.text = aString.toString()
        }
        mainViewModel.getData().observe(viewLifecycleOwner, dataCount)
    }

    private fun readDB() : ArrayList<ItemData>{
        val res = localDB!!.getItem()
        val dataList = ArrayList<ItemData>()
        if(res.count > 0){
            binding.tvEmpty.visibility = View.GONE
            while (res.moveToNext()) {
                val dbTitle = res.getString(0)
                val dbData = res.getString(1)

                val itemData = ItemData(dbTitle, dbData)
                dataList.add(itemData)
            }
        }
        else{
            binding.tvEmpty.visibility = View.VISIBLE
        }
        return dataList
    }

    private fun showRecycler(){
        binding.rvData.setHasFixedSize(true)
        binding.rvData.layoutManager = LinearLayoutManager(context)
        val itemAdapter = RecyclerViewDataAdapter(handlerData, list)
        binding.rvData.adapter = itemAdapter
    }

    @SuppressLint("CheckResult")
    private fun addDialog(title : String?, data : String?){
        dialog.setContentView(R.layout.dialog_data)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.show()

        val edTitle         = dialog.findViewById<EditText>(R.id.edTitle)
        val edData          = dialog.findViewById<EditText>(R.id.edData)
        val btAddUpdate     = dialog.findViewById<Button>(R.id.btAddUpdate)
        val btDeleteItem    = dialog.findViewById<Button>(R.id.btDeleteItem)
        val btCloseDialog   = dialog.findViewById<ImageView>(R.id.btCloseDialog)

        edTitle.setText(title)
        edData.setText(data)

        btAddUpdate.setOnClickListener { addUpdateData(edTitle, edData) }
        btDeleteItem.setOnClickListener { deleteData(edTitle) }
        btCloseDialog.setOnClickListener { dialog.dismiss() }
    }

    private fun addUpdateData(editTitle : EditText, editData : EditText) {
        val title = editTitle.text.toString()
        val data = editData.text.toString()
        if((title.isNotEmpty()) && (data.isNotEmpty())) {
            val stateAdd = localDB!!.inputItem(title, data)
            if(stateAdd){
                Toast.makeText(context, "Command added", Toast.LENGTH_SHORT).show()
                list.clear()
                list.addAll(readDB())
                showRecycler()
                dialog.dismiss()
            }
            else{
                val stateUpdate = localDB!!.updateItem(title, data)
                if (stateUpdate){
                    Toast.makeText(context, "Command updated", Toast.LENGTH_SHORT).show()
                    list.clear()
                    list.addAll(readDB())
                    showRecycler()
                    dialog.dismiss()
                }
            }
        }
        else {
            if(title.isEmpty()){
                editTitle.error = getString(R.string.stringTitleNotValid)
            }
            if (data.isEmpty()){
                editData.error = getString(R.string.stringDataNotValid)
            }
        }
    }

    private fun deleteData(editTitle : EditText) {
        val title = editTitle.text.toString()
        if((title.isNotEmpty())) {
            val stateDelete = localDB!!.deleteItem(title)
            if(stateDelete){
                Toast.makeText(context, "Command deleted", Toast.LENGTH_SHORT).show()
                list.clear()
                list.addAll(readDB())
                showRecycler()
                dialog.dismiss()
            }
            else{
                Toast.makeText(context, "Command does not exist", Toast.LENGTH_SHORT).show()
            }
        }
        else {
            if(title.isEmpty()){
                editTitle.error = getString(R.string.stringTitleNotValid)
            }
        }
    }

    private val handlerData = object:  Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when(msg.what){
                DataVar.dbEdit     -> {
                    val msgTitle = msg.data.getString(DataVar.dbTitle)
                    val msgData = msg.data.getString(DataVar.dbData)
                    addDialog(msgTitle, msgData)
                }
                DataVar.dbDelete   -> {
                    val msgTitle = msg.data.getString(DataVar.dbTitle)
                    //val msgData = msg.data.getString(DataVar.dbData)
                    val stateDelete = localDB!!.deleteItem(msgTitle)
                    if(stateDelete){
                        Toast.makeText(context, "Command deleted", Toast.LENGTH_SHORT).show()
                        list.clear()
                        list.addAll(readDB())
                        showRecycler()
                    }
                }
                DataVar.dbSend     -> {
                    //val msgTitle = msg.data.getString(DataVar.dbTitle)
                    val msgData = msg.data.getString(DataVar.dbData)
                    mainViewModel.setSendData(msgData.toString())
                }
            }
        }
    }

}