package com.example.core.data.source.remote.network

import com.example.core.BuildConfig
import com.example.core.BuildConfig.BASE_URL
import com.example.core.data.source.paging.ForecastNextMonthAdapter
import com.example.core.data.source.remote.response.ForecastNextMonth
import com.example.core.data.source.remote.response.LoginResponse
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class Config {
    companion object {
        fun getApiService(): API {
            val authInterceptor = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()
            val gson = GsonBuilder()
                .registerTypeAdapter(ForecastNextMonth::class.java, ForecastNextMonthAdapter())
                .create()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
            return retrofit.create(API::class.java)
        }

    }
}
