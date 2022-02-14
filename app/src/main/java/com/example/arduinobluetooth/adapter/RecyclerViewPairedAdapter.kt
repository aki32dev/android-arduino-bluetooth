package com.example.arduinobluetooth.adapter

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.arduinobluetooth.R
import com.example.arduinobluetooth.data.DataVar


class RecyclerViewPairedAdapter(
    private val handler   : Handler,
    private val inputName : ArrayList<String>,
    private val inputMac  : ArrayList<String>) : RecyclerView.Adapter<RecyclerViewPairedAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_device_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvName.setText(inputName.get(position))
        holder.tvMac.setText(inputMac.get(position))

        holder.btConnect.setOnClickListener { sendMessage(position) }
    }

    override fun getItemCount() : Int = inputName.size

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var tvName      : TextView = itemView.findViewById(R.id.tvName)
        var tvMac       : TextView = itemView.findViewById(R.id.tvMac)
        var btConnect   : Button   = itemView.findViewById(R.id.btConnect)
    }

    private fun sendMessage(number : Int){
        val message: Message = handler.obtainMessage(DataVar.messageConnect)
        val bundle = Bundle()
        bundle.putString(DataVar.deviceName, inputName.get(number))
        bundle.putString(DataVar.deviceMac, inputMac.get(number))
        message.setData(bundle)
        handler.sendMessage(message)
    }
}