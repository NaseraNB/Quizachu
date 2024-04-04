package com.example.quizachu.pokeApi

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
data class PokeApiResponse(
    @SerializedName("results") val results: List<Pokemon>
)
