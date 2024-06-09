package com.mismayilov.reports.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mismayilov.common.extensions.toBlurText
import com.mismayilov.core.generics.MyDiffUtil
import com.mismayilov.domain.entities.local.IconModel
import com.mismayilov.reports.databinding.ReportAccountListItemDesignBinding
import com.mismayilov.uikit.R
import com.mismayilov.uikit.util.getResourceIdByName

class ReportAccountListRecyclerAdapter constructor(val showBalance:Boolean = false): ListAdapter<IconModel, ReportAccountListRecyclerAdapter.AccountListViewHolder>(MyDiffUtil<IconModel>(
    itemsTheSame = { oldItem, newItem -> oldItem == newItem },
    contentsTheSame = { oldItem, newItem -> oldItem == newItem }
)) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountListViewHolder {
        val binding = ReportAccountListItemDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccountListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AccountListViewHolder(private val binding: ReportAccountListItemDesignBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(iconModel: IconModel) {
            binding.apply {
                iconImage.setImageResource(getResourceIdByName(iconImage.context, iconModel.icon))
                accountNameTxt.text = iconModel.name
                balanceTxt.text = iconModel.balance.toString()
               iconCardview.background =  iconCardview.context.getDrawable( R.drawable.icon_unselected_backround)

                balanceTxt.toBlurText(showBalance)
            }
        }
    }
}