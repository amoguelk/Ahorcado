package com.amog.ahorcado.model

import com.google.gson.annotations.SerializedName

data class Word(
    @SerializedName("word")
    var word: String? = null,
    @SerializedName("category")
    var category: String? = null
)
