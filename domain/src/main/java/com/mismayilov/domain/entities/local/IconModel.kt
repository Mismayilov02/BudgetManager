package com.mismayilov.domain.entities.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "icon")
data class IconModel (
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "icon")
    val icon: String,
    @ColumnInfo(name = "type")
    var type: String,
    @ColumnInfo(name = "balance")
    var balance: String?  = null,
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Long=0,
)