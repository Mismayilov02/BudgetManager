package com.abbtech.firstabbtechapp.modules

import com.abbtech.firstabbtechapp.data.repository.AccountRepositoryImpl
import com.abbtech.firstabbtechapp.data.repository.CategoryRepositoryImpl
import com.abbtech.firstabbtechapp.data.repository.TransactionRepositoryImpl
import com.abbtech.firstabbtechapp.domain.repositories.TransactionRepository
import com.mismayilov.domain.repositories.AccountRepository
import com.mismayilov.domain.repositories.CategoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun bindTransactionRepository(transactionRepositoryImpl: TransactionRepositoryImpl): TransactionRepository

    @Binds
    fun bindAccountRepository(accountRepositoryImpl: AccountRepositoryImpl): AccountRepository

    @Binds
    fun bindCategoryRepository(categoryRepositoryImpl: CategoryRepositoryImpl): CategoryRepository
}