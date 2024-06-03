package com.mismayilov.domain.repositories

interface ExchangeRepository {
    suspend fun getExchange(apiKey: String, host: String, from: String, to: String): String
}