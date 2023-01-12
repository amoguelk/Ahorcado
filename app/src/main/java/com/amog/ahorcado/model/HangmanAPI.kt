package com.amog.ahorcado.model

import retrofit2.Call
import retrofit2.http.GET

interface HangmanAPI {
    @GET("hangman.php")
    fun getWord(): Call<Word>
}