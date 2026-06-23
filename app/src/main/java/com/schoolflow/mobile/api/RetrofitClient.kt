package com.schoolflow.mobile.api

import com.schoolflow.mobile.BuildConfig
import com.schoolflow.mobile.utils.SessionManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private var retrofit: Retrofit? = null
    private var sessionManager: SessionManager? = null

    fun init(sessionManager: SessionManager) {
        this.sessionManager = sessionManager
        retrofit = null
    }

    private fun getAuthInterceptor(): Interceptor {
        return Interceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            requestBuilder.addHeader("Accept", "application/json")
            requestBuilder.addHeader("Content-Type", "application/json")

            sessionManager?.getToken()?.let { token ->
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }

            chain.proceed(requestBuilder.build())
        }
    }

    private fun buildRetrofit(): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(getAuthInterceptor())
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getApi(): SchoolFlowApi {
        if (retrofit == null) {
            retrofit = buildRetrofit()
        }
        return retrofit!!.create(SchoolFlowApi::class.java)
    }
}
