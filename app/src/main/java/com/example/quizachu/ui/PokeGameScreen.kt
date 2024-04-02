package com.example.quizachu.ui

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.quizachu.R
import com.example.quizachu.models.PokeInfoViewModel
import com.example.quizachu.pokeApi.Pokemon
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*


class PokeGameScreen : AppCompatActivity() {
    companion object {
        const val MAX_QUESTIONS_PER_LEVEL = 5
        const val NUM_LEVELS = 3
    }

    //Variables d'iniciació
    private lateinit var viewModel: PokeInfoViewModel
    private lateinit var imageView: ImageView
    private lateinit var buttons: List<Button>
    private var numQuestionsAsked = 0
    private var gameJob: Job? = null
    private lateinit var pokemonList: List<Pokemon>
    private lateinit var selectedPokemon: Pokemon
    private var currentLevel = 1
    private var score = 0

    //Mètodes onCreate y initUI
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poke_game_screen)

        imageView = findViewById(R.id.imagePokemon)
        buttons = listOf(
            findViewById(R.id.btnPokemon1),
            findViewById(R.id.btnPokemon2),
            findViewById(R.id.btnPokemon3)
        )
        viewModel = ViewModelProvider(this)[PokeInfoViewModel::class.java]
        initUI()
    }

    private fun initUI() {
        askQuestion()
    }

    private fun askQuestion() {
        //Sol·licitar llista de pokémons
        viewModel.getPokemonListAndInfo()
        //Observar la variable pokemonInfo i obtenir les dades
        viewModel.pokemonInfo.observe(this) { newList ->
            if (newList.isNotEmpty()) {
                //Guardar la llista a una variable global per ferla servir a tota la classe
                pokemonList = newList

                //Seleccionar un pokémon aleatori
                selectedPokemon = pokemonList.random()

                //Log.d("PokeGame", "Pokémon seleccionado: ${selectedPokemon.name}, Lista completa: $pokemonList")

                //Carregar la imatge del pokémon seleccionat
                loadPokemonImage()

                //Actualitzar els botons amb els noms dels pokémons
                updateButtonNames()
            }
        }
    }

    private fun updateButtonNames() {
        //Assegurar-se que hi ha suficients noms pels botons
        if (pokemonList.size >= buttons.size) {
            buttons.forEachIndexed { index, button ->
                button.text = pokemonList[index].name
                button.setOnClickListener {
                    handleButtonClick(button.text.toString())
                }
            }
        }
    }
    private fun handleButtonClick(clickedPokemonName: String) {
        Picasso.get().load(selectedPokemon.sprites.frontDefault).into(imageView)

        // Actualiza la puntuación y decide si cargar la siguiente pregunta
        updateScore(clickedPokemonName == selectedPokemon.name)
        if (numQuestionsAsked < MAX_QUESTIONS_PER_LEVEL) {
            // Espera y carga la siguiente pregunta
            prepareNextQuestion()
        } else {
            // Finaliza el nivel
            endRound()
        }
    }
    private fun loadPokemonImage() {
        Picasso.get().load(selectedPokemon.sprites.frontDefault).into(imageView, object : Callback {
            override fun onSuccess() {
                applyImageFilter()
            }
            override fun onError(e: Exception?) {
                Toast.makeText(this@PokeGameScreen, "Error al carregar la imatge.", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun applyImageFilter(){
        val levels = intArrayOf(0,2,5)
        val drawable = imageView.drawable as BitmapDrawable
        val bitmap = drawable.bitmap

        val resultBitmap = if(currentLevel>1){
            pixelateImage(applyBacklitFilter(bitmap), pixelSize = levels[currentLevel-1])

        }else{
            applyBacklitFilter(bitmap)
        }

        //Es carrega a la imageView el resultat del bitmap
        imageView.setImageBitmap(resultBitmap)
    }

    //Mètode per crear un bitmap amb els bits en negre
    private fun applyBacklitFilter(bitmap: Bitmap): Bitmap {
        val resultBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)

        //S'agafa cada bit i es posa en negre, en cas que sigui transparent es posa transparent
        for (x in 0 until bitmap.width) {
            for (y in 0 until bitmap.height) {
                val pixelColor = bitmap.getPixel(x, y)
                resultBitmap.setPixel(
                    x, y,
                    if (Color.alpha(pixelColor) != 0) Color.BLACK else Color.TRANSPARENT
                )
            }
        }
        return resultBitmap
    }

    private fun pixelateImage(bitmap: Bitmap, pixelSize: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val pixelated = Bitmap.createBitmap(width, height, bitmap.config)

        for (x in 0 until width step pixelSize) {
            for (y in 0 until height step pixelSize) {
                val pixel = bitmap.getPixel(x, y)
                for (dx in x until (x + pixelSize)) {
                    for (dy in y until (y + pixelSize)) {
                        if (dx < width && dy < height) {
                            pixelated.setPixel(dx, dy, pixel)
                        }
                    }
                }
            }
        }

        return pixelated
    }

    private fun prepareNextQuestion() {
        //Es verifica si hi ha alguna tasca en execució i la cancel·la per evitar errors
        gameJob?.cancel()
        //Crea una nova tasca per cridar la següent pregunta
        gameJob = CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            askQuestion()
        }
    }

    private fun updateScore(isCorrect: Boolean) {
        val pokeCorrect = selectedPokemon.name
        if (isCorrect) {
            score++
            Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "¡Incorrecto! El Pokémon es $pokeCorrect", Toast.LENGTH_SHORT).show()
        }

        numQuestionsAsked++
        if (numQuestionsAsked == MAX_QUESTIONS_PER_LEVEL) {
            endRound()
        }
    }
    private fun endRound() {
        // Mostrar la puntuación y permitir al usuario volver a la pantalla de niveles
        showScoreScreen()

        if (currentLevel > NUM_LEVELS) {
            // Fin del juego, mostrar resultados finales o reiniciar
        }
    }

    private fun showScoreScreen() {
        // Lógica para mostrar la pantalla de puntuación
    }
}
