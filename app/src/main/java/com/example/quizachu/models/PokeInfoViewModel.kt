package com.example.quizachu.models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizachu.pokeApi.ApiService
import com.example.quizachu.pokeApi.PokeApiResponse
import com.example.quizachu.pokeApi.Pokemon
import com.example.quizachu.pokeApi.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokeInfoViewModel : ViewModel() {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service: ApiService = retrofit.create(ApiService::class.java)

    //La variable pokemonInfo amb un SingleLiveEven per enviar actualitzacions només una vegada, així no es dupliquen els pokémons
    val pokemonInfo = SingleLiveEvent<List<Pokemon>>()
    val errorMessage = MutableLiveData<String>()

    fun getPokemonListAndInfo() {
        val randomOffset = (0..898).random() // Generar un offset aleatorio

        val call = service.getPokemonList(limit = 3, offset = randomOffset)
        call.enqueue(object : Callback<PokeApiResponse> {
            override fun onResponse(call: Call<PokeApiResponse>, response: Response<PokeApiResponse>) {
                if (response.isSuccessful) {
                    val pokeApiResponse = response.body()
                    val pokemonList = mutableListOf<Pokemon>()
                    pokeApiResponse?.results?.forEachIndexed { index, pokeResult ->
                        val pokemonId = randomOffset + index + 1 // id de Pokémon basado en el offset y el índice
                        val pokemonInfoCall = service.getPokemonInfo(pokemonId)
                        pokemonInfoCall.enqueue(object : Callback<Pokemon> {
                            override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
                                if (response.isSuccessful) {
                                    response.body()?.let { pokemonList.add(it) }
                                    if (pokemonList.size == 3) {
                                        pokemonInfo.postValue(pokemonList)
                                    }
                                } else {
                                    Log.e("PokeInfoViewModel", "Error al obtener información del Pokémon: ${response.errorBody()?.string()}")
                                    errorMessage.postValue("Error al obtener información del Pokémon.")
                                }
                            }

                            override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                                Log.e("PokeInfoViewModel", "Fallo al realizar la llamada a la API para obtener información del Pokémon", t)
                                errorMessage.postValue("Fallo al realizar la llamada a la API para obtener información del Pokémon.")
                            }
                        })
                    }
                } else {
                    Log.e("PokeInfoViewModel", "Error al obtener la lista de Pokémon: ${response.errorBody()?.string()}")
                    errorMessage.postValue("Error al obtener la lista de Pokémon.")
                }
            }

            override fun onFailure(call: Call<PokeApiResponse>, t: Throwable) {
                Log.e("PokeInfoViewModel", "Fallo al realizar la llamada a la API para obtener la lista de Pokémon", t)
                errorMessage.postValue("Fallo al realizar la llamada a la API para obtener la lista de Pokémon.")
            }
        })
    }
}
