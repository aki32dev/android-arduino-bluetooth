package com.example.arduinobluetooth.adapter

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.arduinobluetooth.R
import com.example.arduinobluetooth.data.DataVar
import com.example.arduinobluetooth.data.ItemData

class RecyclerViewDataAdapter (
    private val handler     : Handler,
    private val itemData    : ArrayList<ItemData>) : RecyclerView.Adapter<RecyclerViewDataAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent  : ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_data_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder : MyViewHolder, position: Int) {
        val (title, data) = itemData[position]
        holder.tvDataTitle.text = title
        holder.tvDataSend.text = data
        holder.btDataEdit.setOnClickListener { actionDb(DataVar.dbEdit, title, data) }
        holder.btDataDelete.setOnClickListener { actionDb(DataVar.dbDelete, title, data) }
        holder.btDataSend.setOnClickListener { actionDb(DataVar.dbSend, title, data) }
    }

    override fun getItemCount() : Int = itemData.size

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var tvDataTitle     : TextView = itemView.findViewById(R.id.tvDataTitle)
        var tvDataSend      : TextView = itemView.findViewById(R.id.tvDataSend)
        var btDataEdit      : ImageView = itemView.findViewById(R.id.btDataEdit)
        var btDataDelete    : ImageView = itemView.findViewById(R.id.btDataDelete)
        var btDataSend      : ImageView = itemView.findViewById(R.id.btDataSend)
    }

    private fun actionDb(address : Int, title : String?, data : String?){
        val message: Message = handler.obtainMessage(address)
        val bundle = Bundle()
        bundle.putString(DataVar.dbTitle, title)
        bundle.putString(DataVar.dbData, data)
        message.data = bundle
        handler.sendMessage(message)
    }
}