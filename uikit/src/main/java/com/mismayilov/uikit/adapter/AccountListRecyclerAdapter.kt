package com.mismayilov.uikit.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mismayilov.core.generics.MyDiffUtil
import com.mismayilov.domain.entities.local.IconModel
import com.mismayilov.uikit.R
import com.mismayilov.uikit.databinding.AccountListItemDesignBinding
import com.mismayilov.uikit.databinding.IconviewItemDesignBinding
import com.mismayilov.uikit.util.getResourceIdByName

class AccountListRecyclerAdapter : ListAdapter<IconModel, AccountListRecyclerAdapter.AccountListViewHolder>(MyDiffUtil<IconModel>(
    itemsTheSame = { oldItem, newItem -> oldItem == newItem },
    contentsTheSame = { oldItem, newItem -> oldItem == newItem }
)) {
    var selectedPosition = 0

    fun setSelectedPosition(iconName: String) {
        val position = currentList.indexOfFirst { it.icon == iconName }
        if (position != -1) {
            selectedPosition = position
            notifyItemChanged(selectedPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountListViewHolder {
        val binding = AccountListItemDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccountListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AccountListViewHolder(private val binding: AccountListItemDesignBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(iconModel: IconModel) {
            binding.apply {
                iconImage.setImageResource(getResourceIdByName(iconImage.context, iconModel.icon))
                accountNameTxt.text = iconModel.name
                balanceTxt.text = iconModel.balance!!
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

private fun String.toCurrencyString(): String {
    if (this.toDouble() % 1 == 0.0) return String.format("%.0f", this.toDouble())
    return String.format("%.2f", this.toDouble())
}
