package com.mismayilov.domain.entities.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.mismayilov.common.unums.TransactionType
import com.mismayilov.domain.entities.converters.TransactionTypeConverters

@Entity(tableName = "category")
data class CategoryModel (
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "icon")
    val icon: String,
    @ColumnInfo(name = "type")
//    @TypeConverters(TransactionTypeConverters::class)
    var type: TransactionType,
    @PrimaryKey(autoGenerate = true)
    val id: Long=0,
)