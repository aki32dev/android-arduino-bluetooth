package com.arbl.arduinobluetooth.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arbl.arduinobluetooth.core.domain.model.PairedModel
import com.arbl.arduinobluetooth.databinding.ItemPairedBinding

class PairedAdapter(
    private val onItemClick : ((PairedModel) -> Unit)? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<PairedModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ContactViewHolder(ItemPairedBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = items[position]
        if (holder is ContactViewHolder) {
            holder.bind(model)
        }
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(data: List<PairedModel>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    inner class ContactViewHolder(private val binding: ItemPairedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: PairedModel) {
            with(binding) {
                tvName.text = model.name
                tvMac.text = model.mac
                btnConnect.setOnClickListener {
                    onItemClick?.invoke(model)
                }
            }
        }
    }
}