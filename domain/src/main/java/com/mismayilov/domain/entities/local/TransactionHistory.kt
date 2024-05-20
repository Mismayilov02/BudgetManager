package com.mismayilov.domain.entities.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_history")
data class TransactionHistory(
    @ColumnInfo(name = "amount")
    val amount: Double,
    @ColumnInfo(name = "amount_usd")
    val amountUsd: Double,
    @ColumnInfo(name = "account_id")
    val account: TransactionAccount?,
    @ColumnInfo(name = "date")
    val date: Long,

    @ColumnInfo(name = "category_id")
    val category: TransactionCategory,
    @ColumnInfo(name = "note")
    val note: String? =null,
    @ColumnInfo(name = "account_to")
    val accountTo: TransactionAccount? = null,
    @ColumnInfo(name = "amount_to")
    val amountTo: Double? = null,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)