package com.mismayilov.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mismayilov.core.generics.MyDiffUtil
import com.mismayilov.common.unums.IconType
import com.mismayilov.common.utility.Util.Companion.DATE_TIME_FORMAT
import com.mismayilov.domain.entities.local.TransactionModel
import com.mismayilov.home.R
import com.mismayilov.uikit.databinding.TransactionHistoryItemDesignBinding
import com.mismayilov.uikit.util.getResourceIdByName
import java.text.SimpleDateFormat

class RecentTransactionAdapter:ListAdapter<TransactionModel, RecentTransactionAdapter.ViewHolder>(
    MyDiffUtil<TransactionModel>(
    itemsTheSame = { oldItem, newItem -> oldItem == newItem },
    contentsTheSame = { oldItem, newItem -> oldItem == newItem }
)) {

    @SuppressLint("SimpleDateFormat")
    private val simpleDateFormat = SimpleDateFormat(DATE_TIME_FORMAT)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TransactionHistoryItemDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: TransactionHistoryItemDesignBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(transactionHistory: TransactionModel) {
            binding.apply {
                val isIncome = IconType.valueOf(transactionHistory.category.type) == IconType.INCOME
                val amountIcon:String = if (isIncome) "+" else "-"

                historyTitle.text = transactionHistory.note?: transactionHistory.category.name
                historyAmount.text = "${amountIcon}${transactionHistory.amount}"

                val color = if (isIncome) com.mismayilov.uikit.R.color.btn_main else com.mismayilov.uikit.R.color.red
                historyAmount.setTextColor(ContextCompat.getColor(historyAmount.context, color))
                historyCarview.setCardBackgroundColor(ContextCompat.getColor(historyCarview.context, color))

                historyDate.text = simpleDateFormat.format(transactionHistory.date)

                val context = historyImage.context
                val resID: Int = getResourceIdByName(context, transactionHistory.category.icon)
                historyImage.setImageResource(resID)

                if (layoutPosition == itemCount - 1) {
                    (historyBaseCarview.layoutParams as? ViewGroup.MarginLayoutParams)?.bottomMargin = 220
                    historyBaseCarview.requestLayout()
                }
            }
        }
    }
}