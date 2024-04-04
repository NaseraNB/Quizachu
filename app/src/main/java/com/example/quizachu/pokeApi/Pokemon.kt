package com.example.quizachu.pokeApi

import com.google.gson.annotations.SerializedName

//Objecte Pokémon
data class Pokemon(
    @SerializedName("name") val name: String,
    @SerializedName("sprites") val sprites: Sprites
)

data class Sprites(
    @SerializedName("front_default") val frontDefault: String?
)
