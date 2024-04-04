package com.example.quizachu.pokeApi

import android.telecom.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//Interfície amb les consultes per fer a la Pokeapi
interface ApiService {
    @GET("pokemon/{id}")
    fun getPokemonInfo(@Path("id") id: Int): retrofit2.Call<Pokemon>
    @GET("pokemon")
    fun getPokemonList(@Query("limit") limit: Int, @Query("offset") offset: Int): retrofit2.Call<PokeApiResponse>
}