package com.mismayilov.uikit.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mismayilov.core.generics.MyDiffUtil
import com.mismayilov.domain.entities.local.IconModel
import com.mismayilov.uikit.R
import com.mismayilov.uikit.databinding.IconviewItemDesignBinding
import com.mismayilov.uikit.util.getResourceIdByName

class IconListRecyclerAdapter constructor(
    private val showItemName:Boolean = true
):ListAdapter<IconModel, IconListRecyclerAdapter.IconsViewHolder>(
    MyDiffUtil<IconModel>(
    itemsTheSame = { oldItem, newItem -> oldItem == newItem },
    contentsTheSame = { oldItem, newItem -> oldItem == newItem }
)) {
    var selectedPosition =/* RecyclerView.NO_POSITION*/0

    fun setSelectedPosition(iconName: String) {
        val position = currentList.indexOfFirst { it.icon == iconName }
        if (position != -1) {
            selectedPosition = position
            notifyItemChanged(selectedPosition)
        }
    }

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
                iconName.visibility = if (showItemName) ViewGroup.VISIBLE else ViewGroup.GONE
                if (!showItemName){
                    iconName.visibility = ViewGroup.GONE
                }
                iconName.setTextColor(iconName.context.getColor(if (layoutPosition == selectedPosition) R.color.green else R.color.text_main))
                iconCardview.background = iconCardview.context.getDrawable(if (layoutPosition == selectedPosition) R.drawable.icon_selected_backround else R.drawable.icon_unselected_backround)
                iconCardview.setOnClickListener {
                    if (layoutPosition == selectedPosition) return@setOnClickListener
                    notifyItemChanged(selectedPosition)
                    selectedPosition = layoutPosition
                    notifyItemChanged(selectedPosition)
                }
            }
        }
    }
}