package com.abhimanyu.dogsitting.mobile.data.remote

import com.abhimanyu.dogsitting.mobile.data.remote.BookingApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // Emulator → host machine mapping for localhost
    private const val BASE_URL = "http://10.0.2.2:8080/"

    // Logs HTTP requests/responses to Logcat (very useful while developing)
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // OkHttp client with logging attached
    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Retrofit instance configured with:
    // - base URL of your Spring Boot backend
    // - Gson converter for JSON <-> Kotlin
    // - custom OkHttp client
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Ready-to-use API service
    val bookingApiService: BookingApiService = retrofit.create(BookingApiService::class.java)
}
