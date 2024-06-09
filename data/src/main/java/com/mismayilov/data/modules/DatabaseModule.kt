package com.mismayilov.data.modules

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mismayilov.common.unums.AccountType
import com.mismayilov.common.unums.CurrencyType
import com.mismayilov.common.unums.IconType
import com.mismayilov.data.R
import com.mismayilov.data.database.dao.AccountDao
import com.mismayilov.data.database.dao.IconModelDao
import com.mismayilov.data.database.db.AppDatabase
import com.mismayilov.domain.entities.local.IconModel
import com.mismayilov.data.database.dao.TransactionDao
import com.mismayilov.domain.entities.local.AccountModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext, AppDatabase::class.java, "budget.db"
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                ioThread {
                    val database = provideRoomDatabase(context)
                    insertInitialData(database, context)
                }
            }
        }).build()
    }

    @Provides
    @Singleton
    fun provideTransactionDao(
        appDatabase: AppDatabase
    ): TransactionDao {
        return appDatabase.transactionDao()
    }

    @Provides
    @Singleton
    fun provideIconsDao(
        appDatabase: AppDatabase
    ): IconModelDao {
        return appDatabase.iconModelDao()
    }

    @Provides
    @Singleton
    fun provideAccountDao(
        appDatabase: AppDatabase
    ): AccountDao {
        return appDatabase.accountDao()
    }
    private fun insertInitialData(database: AppDatabase, context: Context) {
        val iconModelDao = database.iconModelDao()
        val accountDao = database.accountDao()

        val incomeIcons = prepopulateCategoryData(context, IconType.INCOME)
        val expenseIcons = prepopulateCategoryData(context, IconType.EXPENSE)
        val initialAccount = prepopulateAccountData()

        iconModelDao.insertAll(incomeIcons + expenseIcons /*+ transferIcons*/)
        accountDao.insertNoSuspend(initialAccount)
    }

    private fun prepopulateCategoryData(context: Context, type: IconType): List<IconModel> {
        val resourceArray = when (type) {
            IconType.INCOME -> com.mismayilov.uikit.R.array.income_icons
            IconType.EXPENSE -> com.mismayilov.uikit.R.array.expense_icons
            IconType.ACCOUNT -> com.mismayilov.uikit.R.array.transfer_icons
        }

        val categoryNames = context.resources.getStringArray(resourceArray)
        return categoryNames.map { name ->
            IconModel(
                name = name.split("_").joinToString(" ") { it.capitalize() },
                icon = name,
                type = type.name
            )
        }
    }

    private fun prepopulateAccountData(): AccountModel {
        return AccountModel(
            name = "Cash",
            currency = CurrencyType.USD.name,
            amount = 0.0,
            amountUsd = 0.0,
            icon = "salary",
            type = AccountType.CASH.name,
            isPinned = true
        )
    }
}

private val IO_EXECUTOR = Executors.newSingleThreadExecutor()

fun ioThread(f: () -> Unit) {
    IO_EXECUTOR.execute(f)
}