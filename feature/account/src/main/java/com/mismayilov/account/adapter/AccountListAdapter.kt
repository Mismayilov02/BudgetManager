package com.mismayilov.account.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mismayilov.account.databinding.AccountItemDesignBinding
import com.mismayilov.core.generics.MyDiffUtil
import com.mismayilov.domain.entities.local.AccountModel
import com.mismayilov.uikit.util.getResourceIdByName

class AccountListAdapter constructor(
    private val onEditClick: ((Long) -> Unit),
    private val onDeleteClick: ((Long) -> Unit),
    private val onViewClick: ((Long) -> Unit),
    private val onPinClick: ((Long) -> Unit)
) :
    ListAdapter<AccountModel, AccountListAdapter.AccountViewHolder>(MyDiffUtil<AccountModel>(
        itemsTheSame = { oldItem, newItem -> oldItem == newItem },
        contentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )) {

    private var pinningPosition = 0
    fun getPinnedPosition() = pinningPosition
    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val binding =
            AccountItemDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountViewHolder(binding)
    }

    inner class AccountViewHolder(private val binding: AccountItemDesignBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(account: AccountModel) {
            binding.apply {
                accountName.text = account.name
                iconImage.setImageResource(getResourceIdByName(itemView.context, account.icon))
                btnPin.setImageResource(
                    if (account.isPinned) {
                        pinningPosition = layoutPosition
                        getResourceIdByName(itemView.context, "pin")
                    } else {
                        getResourceIdByName(itemView.context, "unpin")
                    }
                )

                btnPin.setOnClickListener {
                    onPinClick.invoke(account.id)
                }
                btnEdit.setOnClickListener {
                    onEditClick.invoke(account.id)
                }
                btnTrash.setOnClickListener {
                    onDeleteClick.invoke(account.id)
                }
                accountItem.setOnClickListener {
                    onViewClick.invoke(account.id)
                }

            }
        }
    }
}