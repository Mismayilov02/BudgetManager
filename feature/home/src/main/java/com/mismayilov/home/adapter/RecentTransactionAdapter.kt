package com.mismayilov.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mismayilov.common.unums.CurrencyType
import com.mismayilov.core.generics.MyDiffUtil
import com.mismayilov.common.unums.IconType
import com.mismayilov.common.utility.Util.Companion.DATE_FORMAT
import com.mismayilov.domain.entities.local.TransactionModel
import com.mismayilov.uikit.databinding.TransactionHistoryItemDesignBinding
import com.mismayilov.uikit.util.getResourceIdByName
import java.text.SimpleDateFormat

class RecentTransactionAdapter constructor(val deleteItem: ((Long) -> Unit) ,val viewItem:(Long)->Unit) : ListAdapter<TransactionModel, RecentTransactionAdapter.ViewHolder>(
    MyDiffUtil<TransactionModel>(
        itemsTheSame = { oldItem, newItem -> oldItem == newItem },
        contentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )) {

    @SuppressLint("SimpleDateFormat")
    private val simpleDateFormat = SimpleDateFormat(DATE_FORMAT)
    private var deleteItemPosition: Int = RecyclerView.NO_POSITION


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TransactionHistoryItemDesignBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: TransactionHistoryItemDesignBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(transactionHistory: TransactionModel) {
            binding.apply {
                val isIncome = IconType.valueOf(transactionHistory.category.type) == IconType.INCOME
                val amountIcon: String = if (isIncome) "+" else "-"
                val currencySymbol = CurrencyType.valueOf(transactionHistory.account?.currency ?: "USD").symbol

                historyTitle.text = transactionHistory.note
                    ?: (transactionHistory.category.name + if(transactionHistory.accountTo != null) " -> ${transactionHistory.accountTo?.name}" else "")
                historyAmount.text = "${amountIcon}${transactionHistory.amount}${currencySymbol}"

                val color =
                    if (isIncome) com.mismayilov.uikit.R.color.btn_main else com.mismayilov.uikit.R.color.red
                historyAmount.setTextColor(ContextCompat.getColor(historyAmount.context, color))
                historyCarview.setCardBackgroundColor(
                    ContextCompat.getColor(
                        historyCarview.context,
                        color
                    )
                )

                historyDesignbaseConstarint.setOnClickListener {
                    viewItem.invoke(transactionHistory.id)
                }

                historyDate.text = simpleDateFormat.format(transactionHistory.date)

                val context = historyImage.context
                val resID: Int = getResourceIdByName(context, transactionHistory.category.icon)
                historyImage.setImageResource(resID)

                (historyBaseCarview.layoutParams as? ViewGroup.MarginLayoutParams)?.bottomMargin =
                    if (layoutPosition == itemCount - 1 && itemCount > 1) 220 else 15
                historyBaseCarview.requestLayout()

            }
        }
    }
    fun deleteItem(position: Int) {
        deleteItem?.invoke(getItem(position).id)
        deleteItemPosition = position
    }

    fun cancelDeleteItem() {
        notifyItemChanged(deleteItemPosition)
        deleteItemPosition = RecyclerView.NO_POSITION
    }

}