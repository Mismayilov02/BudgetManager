package com.mismayilov.domain.entities.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mismayilov.common.unums.AccountType
import com.mismayilov.domain.entities.converters.AccountTypeConverters

@Entity(tableName = "account")
data class AccountModel(
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
//    @TypeConverters(AccountTypeConverters::class)
    var type: AccountType,
    @ColumnInfo(name = "is_pinned")
    val isPinned: Boolean = false,
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)