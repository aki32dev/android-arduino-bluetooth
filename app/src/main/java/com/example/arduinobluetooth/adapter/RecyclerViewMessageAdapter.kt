package com.example.arduinobluetooth.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.arduinobluetooth.R
import com.example.arduinobluetooth.model.MessageModel
import java.text.DateFormat

class RecyclerViewMessageAdapter(private val context: Context, var list: ArrayList<MessageModel>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private inner class MessageInViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var messageTV   : TextView  = itemView.findViewById(R.id.tvMessageOther)
        var dateTV      : TextView  = itemView.findViewById(R.id.tvMessageOtherTime)
        fun bind(position : Int) {
            val messageModel        = list[position]
            messageTV.text          = messageModel.message
            dateTV.text             = DateFormat.getTimeInstance(DateFormat.MEDIUM).format(messageModel.messageTime)
        }
    }

    private inner class MessageOutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var messageTV   : TextView  = itemView.findViewById(R.id.tvMessageSelf)
        var dateTV      : TextView  = itemView.findViewById(R.id.tvMessageSelfTime)
        fun bind(position : Int) {
            val messageModel        = list[position]
            messageTV.text          = messageModel.message
            dateTV.text             = DateFormat.getTimeInstance(DateFormat.MEDIUM).format(messageModel.messageTime)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == MESSAGE_TYPE_IN) {
            MessageInViewHolder(
                LayoutInflater.from(context).inflate(R.layout.message_other, parent, false)
            )
        } else {
            MessageOutViewHolder(
                LayoutInflater.from(context).inflate(R.layout.message_self, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (list[position].messageType == MESSAGE_TYPE_IN) { (holder as MessageInViewHolder).bind(position) }
        else { (holder as MessageOutViewHolder).bind(position) }
    }

    override fun getItemCount() : Int = list.size

    override fun getItemViewType(position: Int): Int { return list[position].messageType }

    companion object {
        const val MESSAGE_TYPE_IN = 1
        const val MESSAGE_TYPE_OUT = 2
    }
}