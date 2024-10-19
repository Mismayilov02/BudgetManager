package com.mismayilov.settings

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mismayilov.common.extensions.toBlurText
import com.mismayilov.core.generics.MyDiffUtil
import com.mismayilov.domain.entities.local.IconModel
import com.mismayilov.settings.databinding.LanguageListItemDesignBinding
import com.mismayilov.uikit.R
import com.mismayilov.uikit.util.getResourceIdByName

class LanguageAdapter constructor(var selectedPosition:Int = 0) : ListAdapter<Pair<String,String>, LanguageAdapter.AccountListViewHolder>(
    MyDiffUtil<Pair<String,String>>(
        itemsTheSame = { oldItem, newItem -> oldItem == newItem },
        contentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountListViewHolder {
        val binding =
            LanguageListItemDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccountListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AccountListViewHolder(private val binding: LanguageListItemDesignBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(language: Pair<String,String>) {
            binding.apply {
                iconImage.setBackgroundResource(getResourceIdByName(iconImage.context, language.second))
                accountNameTxt.text = language.first
                languageCheckedIcon.isVisible = layoutPosition == selectedPosition
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