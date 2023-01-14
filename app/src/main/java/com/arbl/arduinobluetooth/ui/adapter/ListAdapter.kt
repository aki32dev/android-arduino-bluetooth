package com.arbl.arduinobluetooth.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arbl.arduinobluetooth.core.domain.model.ListModel
import com.arbl.arduinobluetooth.databinding.ItemListBinding

class ListAdapter(
    private val onEditClick : ((ListModel) -> Unit)? = null,
    private val onDeleteClick : ((ListModel) -> Unit)? = null,
    private val onSendClick : ((ListModel) -> Unit)? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = mutableListOf<ListModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ContactViewHolder(ItemListBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = items[position]
        if (holder is ContactViewHolder) {
            holder.bind(model)
        }
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(data: List<ListModel>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    inner class ContactViewHolder(private val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: ListModel) {
            with(binding) {
                tvCommandTitle.text = model.title
                tvCommandSend.text = model.command

                btnCommandEdit.setOnClickListener {
                    onEditClick?.invoke(model)
                }

                btnCommandDelete.setOnClickListener {
                    onDeleteClick?.invoke(model)
                }

                btnCommandSend.setOnClickListener {
                    onSendClick?.invoke(model)
                }
            }
        }
    }
}