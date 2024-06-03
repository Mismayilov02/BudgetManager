package com.mismayilov.data.repository
import com.mismayilov.data.api.ExchangeService
import com.mismayilov.domain.repositories.ExchangeRepository
import javax.inject.Inject

class ExchangeRepositoryImpl @Inject constructor(
    private val exchangeService: ExchangeService
)
    : ExchangeRepository {
    override suspend fun getExchange(
        apiKey: String,
        host: String,
        from: String,
        to: String
    ): String {
        return exchangeService.getExchangeRate(apiKey, host, from, to)
    }
}