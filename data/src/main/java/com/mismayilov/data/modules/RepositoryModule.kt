package com.mismayilov.data.modules

import com.mismayilov.data.repository.AccountRepositoryImpl
import com.mismayilov.data.repository.CategoryRepositoryImpl
import com.mismayilov.data.repository.TransactionRepositoryImpl
import com.mismayilov.domain.repositories.TransactionRepository
import com.mismayilov.domain.repositories.AccountRepository
import com.mismayilov.domain.repositories.IconRepository
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
    fun bindCategoryRepository(categoryRepositoryImpl: CategoryRepositoryImpl): IconRepository
}