package com.mismayilov.data.modules

import com.mismayilov.data.api.ExchangeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://currency-exchange.p.rapidapi.com"
    private const val TIMEOUT = 30L

    @Provides
    @Singleton
    fun provideHttpLogger() = HttpLoggingInterceptor().also { httpLoggingInterceptor ->
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return try {
            val builder = OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
            builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }



    @Provides
    @Singleton
    fun provideRetrofit(
        onHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(onHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideCurrencyService(
        retrofit: Retrofit
    ): ExchangeService = retrofit.create(ExchangeService::class.java)


}