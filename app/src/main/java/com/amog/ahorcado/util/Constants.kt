package com.amog.ahorcado.util

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Constants {
    const val BASE_URL = "https://www.serverbpw.com/cm/2023-1/"
    const val LOGTAG = "DEBUG_LOGS"

    fun getRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}