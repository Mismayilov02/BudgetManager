package com.mismayilov.domain.entities.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account")
data class AccountModel(
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "currency")
    val currency: String,
    @ColumnInfo(name = "amount")
    val amount: Double,
    @ColumnInfo(name = "amount_usd")
    var amountUsd: Double,
    @ColumnInfo(name = "icon")
    val icon: String,
    @ColumnInfo(name = "type")
    var type: String,
    @ColumnInfo(name = "is_pinned")
    var isPinned: Boolean = false,
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)