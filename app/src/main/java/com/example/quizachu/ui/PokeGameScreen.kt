package com.example.quizachu.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.quizachu.R
import com.example.quizachu.models.PokeInfoViewModel
import com.example.quizachu.pokeApi.Pokemon
import com.squareup.picasso.Picasso

class PokeGameScreen : AppCompatActivity() {
    private lateinit var viewModel: PokeInfoViewModel
    private var currentPokemonName: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poke_game_screen)

        //Crear instància de PokeInfoViewModel
        viewModel = ViewModelProvider(this).get(PokeInfoViewModel::class.java)

        initUI()

        //Obtenir la llista de Pokémons
        viewModel.getPokemonListAndInfo()
    }

    private fun initUI() {
        //Guardar els botons
        val buttons = listOf<Button>(findViewById(R.id.button), findViewById(R.id.button2), findViewById(R.id.button3))


        viewModel.pokemonInfo.observe(this, Observer { pokemonList ->
            //TODO Assignar els noms dels Pokémons als botons - Hemos de conseguir que no sea correcto el primer botón siempre
            pokemonList.forEachIndexed { index, pokemon ->
                buttons[index].text = pokemon.name
            }

            //Carregar la imatge del primer Pokémon de la llista - se podría hacer aleatório
            loadPokemonImage(pokemonList[0])
        })

        buttons.forEach { button ->
            button.setOnClickListener {
                val clickedPokemonName = button.text.toString()
                if (currentPokemonName == clickedPokemonName) {
                    button.setBackgroundColor(Color.GREEN)
                    Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show()
                } else {
                    button.setBackgroundColor(Color.RED)
                    Toast.makeText(this, "¡Incorrecto!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //Mètode per mostrar la imatge del pokémon amb Picasso
    private fun loadPokemonImage(pokemon: Pokemon) {
        currentPokemonName = pokemon.name

        //Cercar el lloc de la imatge a la vista
        val imageView = findViewById<ImageView>(R.id.imageView)

        //Carregar la imatge del Pokémon
        Picasso.get().load(pokemon.sprites.frontDefault)
            //.placeholder(R.drawable.placeholder_image) // Imagen de carga predeterminada
            //.error(R.drawable.error_image) // Imagen de error
            .into(imageView)
    }
}