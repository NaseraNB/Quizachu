package com.example.quizachu.ui

import android.graphics.Bitmap
import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.quizachu.R
import com.example.quizachu.models.PokeInfoViewModel
import com.example.quizachu.pokeApi.Pokemon
import kotlin.random.Random

class PokeGameScreen : AppCompatActivity() {
    private lateinit var viewModel: PokeInfoViewModel
    private var currentPokemonName: String? = null
    private lateinit var pokemonSelect: Pokemon
    private var buttonClick = false

    //Generar un nombre aleatori entre 0 i 2 per escollir el pokémon correcte
    private var randomNumber: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poke_game_screen)

        //Crear instància de PokeInfoViewModel
        viewModel = ViewModelProvider(this).get(PokeInfoViewModel::class.java)

        //Generar un nombre aleatori entre 0 i 2 per escollir el pokémon correcte
        randomNumber = Random.nextInt(3)

        //Obtenir la llista de Pokémons
        viewModel.getPokemonListAndInfo()

        initUI()
    }

    private fun initUI() {
        //Guardar els botons
        val buttons = listOf<Button>(findViewById(R.id.btnPokemon1), findViewById(R.id.btnPokemon2), findViewById(R.id.btnPokemon3))

        viewModel.pokemonInfo.observe(this, Observer { pokemonList ->

            //Assignar els noms dels Pokémons als botons
            pokemonList.forEachIndexed { index, pokemon ->
                buttons[index].text = pokemon.name
            }

            pokemonSelect = pokemonList[randomNumber]
            //Carregar la imatge del Pokémon
            loadPokemonImage(pokemonSelect)
        })

        buttons.forEach { button ->
            button.setOnClickListener {
                //Guardem el valor del botó on ha fet clic l'usuari
                val clickedPokemonName = button.text.toString()
                buttonClick = true


                if (currentPokemonName == clickedPokemonName) {
                    button.setBackgroundColor(Color.GREEN)
                    Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show()

                } else {
                    button.setBackgroundColor(Color.RED)
                    val correctPokemonName = currentPokemonName ?: ""
                    Toast.makeText(this, "¡Incorrecto! El Pokémon es $correctPokemonName", Toast.LENGTH_SHORT).show()
                }

                loadPokemonImage(pokemonSelect)
            }
        }
    }

    //Mètode per mostrar la imatge del pokémon amb Picasso
    private fun loadPokemonImage(pokemon: Pokemon) {
        currentPokemonName = pokemon.name

        // Buscar el ImageView donde se mostrará la imagen del Pokémon
        val imageView = findViewById<ImageView>(R.id.imagePokemon)

        // Crear una instancia de Picasso y cargar la imagen del Pokémon
        Picasso.get().load(pokemon.sprites.frontDefault)
            //.placeholder(R.drawable.placeholder_image) // Imagen de carga predeterminada
            //.error(R.drawable.error_image) // Imagen de error
            .into(imageView, object : Callback {
                override fun onSuccess() {
                    if(!buttonClick){
                        // Cuando la carga de la imagen sea exitosa, aplicar el filtro de contraluz
                        applyBacklitFilter(imageView)
                    }

                }

                override fun onError(e: Exception?) {
                    // Manejar el caso de error, si lo hay
                }
            })
    }

    //Mètode per aplicar un filtre negre al pokémon
    private fun applyBacklitFilter(imageView: ImageView) {
        //Obtenir la imatge del ImageView
        val drawable = imageView.drawable

        //Convertir la imatge a Bitmap
        val bitmap = (drawable as BitmapDrawable).bitmap

        //Crear un nou Bitmap per guardar el resultat
        val resultBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)

        //Iterar sobre tots els  píxeles de la imatge
        for (x in 0 until bitmap.width) {
            for (y in 0 until bitmap.height) {
                //Obtenir el color del píxel a la posició (x,y)
                val pixelColor = bitmap.getPixel(x, y)

                //Verificar si el píxel no és transparent
                if (Color.alpha(pixelColor) != 0) {
                    //Si el píxel no és transparent, es posa en negre
                    resultBitmap.setPixel(x, y, Color.BLACK)
                } else {
                    //Si el píxel és transparent, deixar-lo transparent
                    resultBitmap.setPixel(x, y, Color.TRANSPARENT)
                }
            }
        }

        //Posar el Bitmap resultant al ImageView
        imageView.setImageBitmap(resultBitmap)
    }

}