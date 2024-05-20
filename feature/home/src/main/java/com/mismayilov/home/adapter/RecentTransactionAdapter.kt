package com.mismayilov.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abbtech.firstabbtechapp.core.utility.MyDiffUtil
import com.mismayilov.common.utility.Util
import com.mismayilov.domain.entities.local.TransactionHistory
import com.mismayilov.home.R
import com.mismayilov.uikit.databinding.TransactionHistoryItemDesignBinding
import java.text.SimpleDateFormat

class RecentTransactionAdapter:ListAdapter<TransactionHistory, RecentTransactionAdapter.ViewHolder>(MyDiffUtil<TransactionHistory>(
    itemsTheSame = { oldItem, newItem -> oldItem == newItem },
    contentsTheSame = { oldItem, newItem -> oldItem == newItem }
)) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TransactionHistoryItemDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: TransactionHistoryItemDesignBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(transactionHistory: TransactionHistory) {
            binding.apply {
                val isIncome = transactionHistory.category.type == "income"
                val amountIcon:String = if (isIncome) "+" else "-"
                historyTitle.text = transactionHistory.note?: transactionHistory.category.name
                historyAmount.text = "${amountIcon}${transactionHistory.amount}"
                val color = if (isIncome) com.mismayilov.uikit.R.color.btn_main else com.mismayilov.uikit.R.color.red
                historyAmount.setTextColor(ContextCompat.getColor(historyAmount.context, color))
                historyCarview.setCardBackgroundColor(ContextCompat.getColor(historyCarview.context, color))
                historyDate.text = Util.simpleDateFormat.format(transactionHistory.date)
                val context = historyImage.context
                val resID: Int = context.resources.getIdentifier(transactionHistory.category.icon, "drawable", "com.mismayilov.budgetmanager");
                historyImage.setImageResource(resID)
                if(!isIncome) historyImage.rotation = 180f

            }
        }
    }
}