package com.theternal.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.theternal.data.database.dao.TransactionHistoryDao
import com.theternal.domain.entities.local.Note

@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notesDao() : TransactionHistoryDao
}