package com.arbl.arduinobluetooth.adapter

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arbl.arduinobluetooth.R
import com.arbl.arduinobluetooth.data.Constants


class RecyclerViewPairedAdapter(
    private val handler   : Handler,
    private val inputName : ArrayList<String>,
    private val inputMac  : ArrayList<String>) : RecyclerView.Adapter<RecyclerViewPairedAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_device_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvName.text  = inputName[position]
        holder.tvMac.text   = inputMac[position]

        holder.btConnect.setOnClickListener { sendMessage(position) }
    }

    override fun getItemCount() : Int = inputName.size

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var tvName      : TextView = itemView.findViewById(R.id.tvName)
        var tvMac       : TextView = itemView.findViewById(R.id.tvMac)
        var btConnect   : Button   = itemView.findViewById(R.id.btConnect)
    }

    private fun sendMessage(position : Int){
        val message: Message    = handler.obtainMessage(Constants.messageConnect)
        val bundle              = Bundle()
        bundle.putString(Constants.deviceName, inputName[position])
        bundle.putString(Constants.deviceMac, inputMac[position])
        message.data            = bundle
        handler.sendMessage(message)
    }
}