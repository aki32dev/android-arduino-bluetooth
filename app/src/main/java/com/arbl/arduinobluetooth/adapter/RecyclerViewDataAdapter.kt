package com.arbl.arduinobluetooth.adapter

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arbl.arduinobluetooth.R
import com.arbl.arduinobluetooth.data.Constants
import com.arbl.arduinobluetooth.data.ItemData

class RecyclerViewDataAdapter (
    private val handler     : Handler,
    private val itemData    : ArrayList<ItemData>) : RecyclerView.Adapter<RecyclerViewDataAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent  : ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_data_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder : MyViewHolder, position: Int){
        val (title, data)       = itemData[position]
        holder.tvDataTitle.text = title
        holder.tvDataSend.text  = data
        holder.btDataEdit.setOnClickListener    { actionDb(Constants.dbEdit,      title, data) }
        holder.btDataDelete.setOnClickListener  { actionDb(Constants.dbDelete,    title, data) }
        holder.btDataSend.setOnClickListener    { actionDb(Constants.dbSend,      title, data) }
    }

    override fun getItemCount() : Int = itemData.size

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var tvDataTitle     : TextView  = itemView.findViewById(R.id.tvDataTitle)
        var tvDataSend      : TextView  = itemView.findViewById(R.id.tvDataSend)
        var btDataEdit      : ImageView = itemView.findViewById(R.id.btDataEdit)
        var btDataDelete    : ImageView = itemView.findViewById(R.id.btDataDelete)
        var btDataSend      : ImageView = itemView.findViewById(R.id.btDataSend)
    }

    private fun actionDb(address : Int, title : String?, data : String?){
        val message : Message                       = handler.obtainMessage(address)
        val bundle                                  = Bundle()
        bundle.putString(Constants.dbTitle, title)
        bundle.putString(Constants.dbData, data)
        message.data                                = bundle
        handler.sendMessage(message)
    }
}