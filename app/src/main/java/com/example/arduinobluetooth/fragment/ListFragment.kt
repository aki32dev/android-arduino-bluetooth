package com.example.arduinobluetooth.fragment

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
        localDB = LocalDB(context)

        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        subscribe()
        list.addAll(readDB())
        showRecycler()

        binding.btAdd.setOnClickListener { addDialog() }
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
                val dbId = res.getString(0)
                val dbName = res.getString(1)
                val dbData = res.getString(2)

                val itemData = ItemData(dbId, dbName, dbData)
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

    private fun addDialog(){
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

        btCloseDialog.setOnClickListener { dialog.dismiss() }
    }

    private val handlerData = object:  Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when(msg.what){
                DataVar.messageEdit     -> {

                }
                DataVar.messageDelete   -> {

                }
                DataVar.messageSend     -> {

                }
            }
        }
    }

}