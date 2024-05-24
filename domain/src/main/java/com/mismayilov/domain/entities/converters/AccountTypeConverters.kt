package com.mismayilov.domain.entities.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.mismayilov.common.unums.AccountType


class AccountTypeConverters {
    @TypeConverter
    fun fromAccountType(value: AccountType): String {
        return value.name
    }

    @TypeConverter
    fun toAccountType(value: String): AccountType {
        return AccountType.valueOf(value)
    }
}