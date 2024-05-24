package com.mismayilov.domain.entities.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.mismayilov.domain.entities.local.AccountModel


class AccountModelConverters {

    private val gson = Gson()

    @TypeConverter
    fun fromAccountModel(value: AccountModel?): String? {
        return value?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toAccountModel(value: String?): AccountModel? {
        return value?.let { gson.fromJson(it, AccountModel::class.java) }
    }
}