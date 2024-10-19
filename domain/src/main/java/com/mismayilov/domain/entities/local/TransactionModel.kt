package com.mismayilov.domain.entities.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "transaction")
data class TransactionModel(
    @SerializedName("amount")
    @ColumnInfo(name = "amount")
    val amount: Double,
    @ColumnInfo(name = "account")
    @SerializedName("account")
    var account: AccountModel?,
    @ColumnInfo(name = "date")
    @SerializedName("date")
    val date: Long,
    @ColumnInfo(name = "category")
    @SerializedName("category")
    var category: IconModel,
    @ColumnInfo(name = "note")
    @SerializedName("note")
    val note: String? =null,
    @ColumnInfo(name = "account_to")
    @SerializedName("account_to")
    var accountTo: AccountModel? = null,
    @ColumnInfo(name = "amount_to")
    @SerializedName("amount_to")
    val amountTo: Double? = null,
    @SerializedName("amount_to_usd")
    @ColumnInfo(name = "ammount_to_usd")
    val amountToUsd: Double? = null,
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)