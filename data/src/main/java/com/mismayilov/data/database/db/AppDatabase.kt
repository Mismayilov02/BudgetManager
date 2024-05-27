package com.mismayilov.data.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mismayilov.data.database.dao.AccountDao
import com.mismayilov.data.database.dao.IconModelDao
import com.mismayilov.domain.entities.converters.AccountModelConverters
import com.mismayilov.domain.entities.converters.IconModelConverters
import com.mismayilov.domain.entities.local.AccountModel
import com.mismayilov.domain.entities.local.IconModel
import com.mismayilov.domain.entities.local.TransactionModel
import com.mismayilov.data.database.dao.TransactionDao

@Database(
    entities = [TransactionModel::class, IconModel::class, AccountModel::class],
    version = 1
)
@TypeConverters(
    AccountModelConverters::class,
    IconModelConverters::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun iconModelDao(): IconModelDao
    abstract fun accountDao(): AccountDao
}