package com.mismayilov.data.modules

import com.mismayilov.data.repository.ExchangeRepositoryImpl
import com.mismayilov.domain.repositories.ExchangeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ExchangeRepositoryModule {
    @Binds
    fun bindExchangeRepository(exchangeRepositoryImpl: ExchangeRepositoryImpl): ExchangeRepository
}