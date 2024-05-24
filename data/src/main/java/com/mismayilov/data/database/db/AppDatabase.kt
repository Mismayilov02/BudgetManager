package com.mismayilov.data.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mismayilov.data.database.dao.AccountDao
import com.mismayilov.data.database.dao.CategoryDao
import com.mismayilov.domain.entities.converters.AccountModelConverters
import com.mismayilov.domain.entities.converters.AccountTypeConverters
import com.mismayilov.domain.entities.converters.CategoryModelConverters
import com.mismayilov.domain.entities.converters.TransactionTypeConverters
import com.mismayilov.domain.entities.local.AccountModel
import com.mismayilov.domain.entities.local.CategoryModel
import com.mismayilov.domain.entities.local.TransactionModel
import com.theternal.data.database.dao.TransactionDao

@Database(
    entities = [TransactionModel::class, CategoryModel::class, AccountModel::class],
    version = 1
)
@TypeConverters(TransactionTypeConverters::class, CategoryModelConverters::class, AccountModelConverters::class, AccountTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun accountDao(): AccountDao
}