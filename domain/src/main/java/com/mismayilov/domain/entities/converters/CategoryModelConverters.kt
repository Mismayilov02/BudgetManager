package com.mismayilov.domain.entities.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.mismayilov.domain.entities.local.CategoryModel


class CategoryModelConverters {

    private val gson = Gson()

    @TypeConverter
    fun fromCategoryModel(value: CategoryModel): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toCategoryModel(value: String): CategoryModel {
        return gson.fromJson(value, CategoryModel::class.java)
    }
}
