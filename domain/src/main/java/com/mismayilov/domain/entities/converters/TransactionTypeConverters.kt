package com.mismayilov.domain.entities.converters

import androidx.room.TypeConverter
import com.mismayilov.common.unums.TransactionType

class TransactionTypeConverters {

    @TypeConverter
    fun fromTransactionType(value: TransactionType): String {
        return value.name
    }

    @TypeConverter
    fun toTransactionType(value: String): TransactionType {
        return TransactionType.valueOf(value)
    }
}