package com.mismayilov.domain.entities.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "account")
data class AccountModel(
    @ColumnInfo(name = "name")
    @SerializedName("name")
    val name: String,
    @ColumnInfo(name = "currency")
    @SerializedName("currency")
    val currency: String,
    @ColumnInfo(name = "amount")
    @SerializedName("amount")
    val amount: Double,
    @ColumnInfo(name = "amount_usd")
    @SerializedName("amount_usd")
    var amountUsd: Double,
    @ColumnInfo(name = "icon")
    @SerializedName("icon")
    val icon: String,
    @ColumnInfo(name = "type")
    @SerializedName("type")
    var type: String,
    @ColumnInfo(name = "is_pinned")
    @SerializedName("is_pinned")
    var isPinned: Boolean = false,
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)