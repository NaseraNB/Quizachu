package com.example.quizachu.pokeApi

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

//Classe per la resposta de l'API
data class PokeApiResponse(
    @SerializedName("results") val results: List<Pokemon>
)
