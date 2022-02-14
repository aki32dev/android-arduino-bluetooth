package com.example.arduinobluetooth.adapter

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.arduinobluetooth.R

class RecyclerViewDataAdapter (
    private val handler         : Handler,
    private val inputDataName   : ArrayList<String>,
    private val inputDataSend   : ArrayList<String>) : RecyclerView.Adapter<RecyclerViewDataAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent  : ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_data_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder : MyViewHolder, position: Int) {

    }

    override fun getItemCount() : Int = inputDataName.size

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var tvDataName      : TextView = itemView.findViewById(R.id.tvDataName)
        var tvDataSend      : TextView = itemView.findViewById(R.id.tvDataSend)
        var btDataEdit      : ImageView = itemView.findViewById(R.id.btDataEdit)
        var btDataDelete    : ImageView = itemView.findViewById(R.id.btDataDelete)
        var btDataSend      : ImageView = itemView.findViewById(R.id.btDataSend)
    }
}