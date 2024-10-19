package com.mismayilov.domain.entities.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "icon")
data class IconModel (
    @SerializedName("name")
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "icon")
    @SerializedName("icon")
    val icon: String,
    @ColumnInfo(name = "type")
    @SerializedName("type")
    var type: String,
    @ColumnInfo(name = "balance")
    @SerializedName("balance")
    var balance: String?  = null,
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    val id: Long=0,
)