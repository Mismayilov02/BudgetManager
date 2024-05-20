package com.mismayilov.domain.entities.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_account")
data class TransactionAccount(
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "currency")
    val currency: String,
    @ColumnInfo(name = "amount")
    val amount: Double,
    @ColumnInfo(name = "amount_usd")
    val amountUsd: Double,
    @ColumnInfo(name = "icon")
    val icon: String,
    @ColumnInfo(name = "type")
    val type: String,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)