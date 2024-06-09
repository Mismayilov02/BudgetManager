package com.mismayilov.icon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mismayilov.core.generics.MyDiffUtil
import com.mismayilov.domain.entities.local.IconModel
import com.mismayilov.icon.databinding.IconItemDesignBinding
import com.mismayilov.uikit.util.getResourceIdByName

class IconListAdapter constructor(
    private val onEditClick: ((IconModel) -> Unit),
    private val onDeleteClick: ((Long) -> Unit)
) :
    ListAdapter<IconModel, IconListAdapter.IconViewHolder>(MyDiffUtil<IconModel>(
        itemsTheSame = { oldItem, newItem -> oldItem == newItem },
        contentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )) {
    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
        val binding =
            IconItemDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IconViewHolder(binding)
    }

    inner class IconViewHolder(private val binding: IconItemDesignBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(iconModel: IconModel) {
            binding.apply {
                accountName.text = iconModel.name
                iconImage.setImageResource(getResourceIdByName(itemView.context, iconModel.icon))
                btnEdit.setOnClickListener {
                    onEditClick.invoke(iconModel)
                }
                btnTrash.setOnClickListener {
                    onDeleteClick.invoke(iconModel.id)
                }

            }
        }
    }
}