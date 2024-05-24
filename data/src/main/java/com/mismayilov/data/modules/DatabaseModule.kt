package com.mismayilov.data.modules

import android.content.Context
import androidx.room.Room
import com.mismayilov.data.database.dao.AccountDao
import com.mismayilov.data.database.dao.CategoryDao
import com.mismayilov.data.database.db.AppDatabase
import com.mismayilov.domain.entities.converters.AccountModelConverters
import com.mismayilov.domain.entities.converters.AccountTypeConverters
import com.mismayilov.domain.entities.converters.CategoryModelConverters
import com.mismayilov.domain.entities.converters.TransactionTypeConverters
import com.theternal.data.database.dao.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(
        @ApplicationContext context: Context,
//        accountTypeConverters: Class<AccountTypeConverters>,
        accountModelConverters: Class<AccountModelConverters>,
        categoryModelConverters: Class<CategoryModelConverters>,
//        transactionTypeConverters: Class<TransactionTypeConverters>
    ): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext, AppDatabase::class.java, "budgett.db"
        )/*.addTypeConverter(accountTypeConverters)*/.addTypeConverter(accountModelConverters)
            .addTypeConverter(categoryModelConverters)/*.addTypeConverter(transactionTypeConverters)*/
            .build()
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
    fun provideCategoryDao(
        appDatabase: AppDatabase
    ): CategoryDao {
        return appDatabase.categoryDao()
    }

    @Provides
    @Singleton
    fun provideAccountDao(
        appDatabase: AppDatabase
    ): AccountDao {
        return appDatabase.accountDao()
    }

    @Provides
    @Singleton
    fun provideAccountTypeConverters(): Class<AccountTypeConverters> {
        return AccountTypeConverters::class.java
    }

    @Provides
    @Singleton
    fun provideAccountModelConverters(): Class<AccountModelConverters> {
        return AccountModelConverters::class.java
    }

    @Provides
    @Singleton
    fun provideCategoryModelConverters(): Class<CategoryModelConverters> {
        return CategoryModelConverters::class.java
    }

    @Provides
    @Singleton
    fun provideTransactionTypeConverters(): Class<TransactionTypeConverters> {
        return TransactionTypeConverters::class.java
    }

}