package com.amog.ahorcado.model

import retrofit2.Call
import retrofit2.http.GET

interface HangmanAPI {
    @GET
    fun getWord(): Call<Word>
}