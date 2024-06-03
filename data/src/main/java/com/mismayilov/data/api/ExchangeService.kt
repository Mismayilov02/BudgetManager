package com.mismayilov.data.api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ExchangeService {

    @GET("/exchange")
    suspend fun getExchangeRate(
        @Header("x-rapidapi-key") apiKey: String,
        @Header("x-rapidapi-host") host: String,
        @Query("from") from: String,
        @Query("to") to: String
    ): String

}