package com.mismayilov.common.uikit.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mismayilov.common.R
import com.mismayilov.common.databinding.IconviewItemDesignBinding
import com.mismayilov.core.generics.MyDiffUtil
import com.mismayilov.domain.entities.local.IconModel
import com.mismayilov.uikit.util.getResourceIdByName

class IconsRecyclerViewAdapter:ListAdapter<IconModel, IconsRecyclerViewAdapter.IconsViewHolder>(
    MyDiffUtil<IconModel>(
    itemsTheSame = { oldItem, newItem -> oldItem == newItem },
    contentsTheSame = { oldItem, newItem -> oldItem == newItem }
)) {
    public var selectedPosition =/* RecyclerView.NO_POSITION*/0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconsViewHolder {
        val binding = IconviewItemDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IconsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IconsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class IconsViewHolder(private val binding: IconviewItemDesignBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(iconModel: IconModel) {
            binding.apply {
                iconImage.setImageResource(getResourceIdByName(iconImage.context, iconModel.icon))
                iconName.text = iconModel.name
                iconName.setTextColor(iconName.context.getColor(if (layoutPosition == selectedPosition) R.color.green else R.color.text_main))
                iconCardview.background = iconCardview.context.getDrawable(if (layoutPosition == selectedPosition) R.drawable.icon_selected_backround else R.drawable.icon_unselected_backround)
                iconCardview.setOnClickListener {
                    if (layoutPosition == selectedPosition) return@setOnClickListener
                    notifyItemChanged(selectedPosition)
                    selectedPosition = layoutPosition
                    notifyItemChanged(selectedPosition)
                }
               /* iconBase.setOnClickListener {
                    onIconSelected.invoke(iconModel.id.toInt())
                }*/
            }
        }
    }
}