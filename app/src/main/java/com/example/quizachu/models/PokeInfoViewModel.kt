package com.example.quizachu.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizachu.pokeApi.ApiService
import com.example.quizachu.pokeApi.PokeApiResponse
import com.example.quizachu.pokeApi.Pokemon
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokeInfoViewModel() : ViewModel() {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service: ApiService = retrofit.create(ApiService::class.java)

    //Obtenir dades del Pokemon
    val pokemonInfo = MutableLiveData<List<Pokemon>>()

    //Mètode per aconseguir una llista aleatòria de tres Pokémons
    fun getPokemonListAndInfo() {
        val randomOffset = (0..898).random() //Generar un offset aleatori

        val call = service.getPokemonList(limit = 3, offset = randomOffset)
        call.enqueue(object : Callback<PokeApiResponse> {
            override fun onResponse(call: Call<PokeApiResponse>, response: Response<PokeApiResponse>) {
                if (response.isSuccessful) {
                    val pokeApiResponse = response.body()
                    val pokemonList = mutableListOf<Pokemon>()
                    pokeApiResponse?.results?.forEachIndexed { index, pokeResult ->
                        val pokemonId = randomOffset + index + 1 //id de Pokémon basat en l'offset i l'índex
                        val pokemonInfoCall = service.getPokemonInfo(pokemonId)
                        pokemonInfoCall.enqueue(object : Callback<Pokemon> {
                            override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
                                if (response.isSuccessful) {
                                    val pokemon = response.body()
                                    pokemon?.let { pokemonList.add(it) }
                                    if (pokemonList.size == 3) {
                                        pokemonInfo.postValue(pokemonList)
                                    }
                                } else {
                                    //No se que poner
                                    //TODO
                                }
                            }

                            override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                                //TODO
                            }
                        })
                    }
                } else {
                    //TODO
                }
            }

            override fun onFailure(call: Call<PokeApiResponse>, t: Throwable) {
               //TODO
            }
        })
    }
}