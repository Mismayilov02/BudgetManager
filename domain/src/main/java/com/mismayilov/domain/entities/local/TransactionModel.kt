package com.mismayilov.domain.entities.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.mismayilov.domain.entities.converters.AccountModelConverters
import com.mismayilov.domain.entities.converters.CategoryModelConverters

@Entity(tableName = "transaction")
data class TransactionModel(
    @ColumnInfo(name = "amount")
    val amount: Double,
    @ColumnInfo(name = "amount_usd")
    val amountUsd: Double,
    @ColumnInfo(name = "account_id")
    @TypeConverters(AccountModelConverters::class)
    var account: AccountModel?,
    @ColumnInfo(name = "date")
    val date: Long,
    @ColumnInfo(name = "category_id")
    @TypeConverters(CategoryModelConverters::class)
    var category: CategoryModel,
    @ColumnInfo(name = "note")
    val note: String? =null,
    @ColumnInfo(name = "account_to")
    @TypeConverters(AccountModelConverters::class)
    var accountTo: AccountModel? = null,
    @ColumnInfo(name = "amount_to")
    val amountTo: Double? = null,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)