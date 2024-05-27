package com.mismayilov.domain.entities.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.mismayilov.domain.entities.local.IconModel


class IconModelConverters {

    private val gson = Gson()

    @TypeConverter
    fun fromCategoryModel(value: IconModel): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toCategoryModel(value: String): IconModel {
        return gson.fromJson(value, IconModel::class.java)
    }
}
