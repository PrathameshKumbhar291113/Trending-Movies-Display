package com.example.moviedisplayapp.api


import com.example.moviedisplayapp.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ApiService {
// using okhttpclient to keep the logs of the body of the api
    val client = OkHttpClient.Builder().apply {
        if(BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            addInterceptor(loggingInterceptor)
        }
        readTimeout(1, TimeUnit.MINUTES)
        connectTimeout(30, TimeUnit.SECONDS)
        writeTimeout(1, TimeUnit.MINUTES)
    }.build()
// by Moshi we passed the base url to the retrofit
    val restService = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder()
                    .addLast(KotlinJsonAdapterFactory())
                    .build()
            )
        ).build()
        .create(MoviesApi::class.java)
}