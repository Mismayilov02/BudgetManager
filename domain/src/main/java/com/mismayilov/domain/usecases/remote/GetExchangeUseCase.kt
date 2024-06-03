package com.mismayilov.domain.usecases.remote

import com.mismayilov.domain.repositories.ExchangeRepository
import javax.inject.Inject

class GetExchangeUseCase @Inject constructor(
    private val exchangeRepository: ExchangeRepository
) {
    suspend operator fun invoke(from: String, to: String): String {
        return exchangeRepository.getExchange(
            apiKey = "7f33248a31msh187cac9e8aa82b1p122780jsn7338fc89dfe4",
            host = "currency-exchange.p.rapidapi.com",
            from, to)
    }
}