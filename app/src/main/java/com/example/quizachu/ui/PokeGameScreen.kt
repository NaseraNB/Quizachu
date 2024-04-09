package com.example.quizachu.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.quizachu.Menu
import com.example.quizachu.R
import com.example.quizachu.RecyclerView
import com.example.quizachu.models.PokeInfoViewModel
import com.example.quizachu.pokeApi.Pokemon
import com.example.quizachu.seleccio_De_Nivells
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PokeGameScreen : AppCompatActivity() {
    companion object {
        const val MAX_QUESTIONS_PER_LEVEL = 5
        const val NUM_LEVELS = 3
    }

    // Variables de inicialización
    private lateinit var viewModel: PokeInfoViewModel
    private lateinit var imageView: ImageView
    private lateinit var buttons: List<Button>
    private lateinit var button_exit: Button
    private var numQuestionsAsked = 0
    private var gameJob: Job? = null
    private lateinit var pokemonList: List<Pokemon>
    private lateinit var selectedPokemon: Pokemon
    private var currentLevel = 1
    private var score = 0

    // Variables de usuario
    private var NOM: String = ""
    private var PUNTUACIO: String = ""
    private var UID: String = ""
    private var NIVELL: String = ""

    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var jugadors: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poke_game_screen)

        firebaseDatabase = FirebaseDatabase.getInstance()
        jugadors = firebaseDatabase.getReference("DATA BASE JUGADORS")

        var intent: Bundle? = getIntent().extras
        UID = intent?.getString("UID").toString()
        NOM = intent?.getString("NOM").toString()
        PUNTUACIO = intent?.getString("PUNTUACIO").toString()
        NIVELL = intent?.getString("NIVELL").toString()

        currentLevel = NIVELL.toInt()

        imageView = findViewById(R.id.imagePokemon)
        buttons = listOf(
            findViewById(R.id.btnPokemon1),
            findViewById(R.id.btnPokemon2),
            findViewById(R.id.btnPokemon3)
        )
        button_exit = findViewById(R.id.sortidaJoc)

        viewModel = ViewModelProvider(this)[PokeInfoViewModel::class.java]
        initUI()

        button_exit.setOnClickListener {
            showEndGameDialog() // Muestra el diálogo de fin de juego al hacer clic en el botón de salida
        }
    }

    private fun initUI() {
        askQuestion()
    }

    private fun askQuestion() {
        viewModel.getPokemonListAndInfo()
        viewModel.pokemonInfo.observe(this) { newList ->
            if (newList.isNotEmpty()) {
                pokemonList = newList
                selectedPokemon = pokemonList.random()
                loadPokemonImage()
                updateButtonNames()
            }
        }
    }

    private fun updateButtonNames() {
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
        updateScore(clickedPokemonName == selectedPokemon.name)
        if (numQuestionsAsked < MAX_QUESTIONS_PER_LEVEL) {
            prepareNextQuestion()
        } else {
            endRound()
        }
    }

    private fun loadPokemonImage() {
        Picasso.get().load(selectedPokemon.sprites.frontDefault).into(imageView, object : Callback {
            override fun onSuccess() {
                applyImageFilter()
            }

            override fun onError(e: Exception?) {
                Toast.makeText(this@PokeGameScreen, "Error loading image.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun applyImageFilter() {
        val levels = intArrayOf(0, 2, 5)
        val drawable = imageView.drawable as BitmapDrawable
        val bitmap = drawable.bitmap

        val resultBitmap = if (currentLevel > 1) {
            pixelateImage(applyBacklitFilter(bitmap), pixelSize = levels[currentLevel - 1])

        } else {
            applyBacklitFilter(bitmap)
        }

        imageView.setImageBitmap(resultBitmap)
    }

    private fun applyBacklitFilter(bitmap: Bitmap): Bitmap {
        val resultBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)

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
        gameJob?.cancel()
        gameJob = CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            askQuestion()
        }
    }

    private fun updateScore(isCorrect: Boolean) {
        val pokeCorrect = selectedPokemon.name
        if (isCorrect) {
            score++
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Incorrect! The Pokémon is $pokeCorrect", Toast.LENGTH_SHORT).show()
        }

        numQuestionsAsked++
        if (numQuestionsAsked == MAX_QUESTIONS_PER_LEVEL) {
            endRound()
        }
    }

    private fun endRound() {
        showEndGameDialog()
    }

    private fun showEndGameDialog() {
        val endGameDialog = Dialog(this@PokeGameScreen)
        endGameDialog.setContentView(R.layout.activity_fi_del_joc)

        val fiDelJoc = endGameDialog.findViewById<TextView>(R.id.fiDelJoc)
        val TotalDePuntuacio = endGameDialog.findViewById<TextView>(R.id.TotalDePuntuacio)
        val NombrePuntacico = endGameDialog.findViewById<TextView>(R.id.NombrePuntacico)
        val Seleccio_Nivells = endGameDialog.findViewById<Button>(R.id.Seleccio_Nivells)
        val menu = endGameDialog.findViewById<Button>(R.id.jugarDeNou)
        val Puntatges = endGameDialog.findViewById<Button>(R.id.Puntatges)

        NombrePuntacico.text = score.toString()

        endGameDialog.show()

        guardarPuntuacion(score.toString()) // Aquí se guarda la puntuación

        menu.setOnClickListener {
            val intent = Intent(this@PokeGameScreen, Menu::class.java)
            startActivity(intent)
            finish()
        }

        Seleccio_Nivells.setOnClickListener {
            val intent = Intent(this@PokeGameScreen, seleccio_De_Nivells::class.java)
            intent.putExtra("UID", UID)
            intent.putExtra("NOM", NOM)
            intent.putExtra("PUNTUACIO", PUNTUACIO)
            intent.putExtra("NIVELL", NIVELL)
            startActivity(intent)
        }


        Puntatges.setOnClickListener {
            val intent = Intent(this@PokeGameScreen, RecyclerView::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun guardarPuntuacion(puntuacion: String) {
        val uidString: String? = UID // Obtener el UID del usuario
        if (uidString != null) {
            // Obtener la puntuación actual almacenada en la base de datos
            jugadors.child(uidString).child("Puntuacio").get().addOnSuccessListener { dataSnapshot ->
                val puntuacionActual = dataSnapshot.value.toString().toInt()
                // Sumar la puntuación actual del nivel al total almacenado en la base de datos
                val nuevaPuntuacionTotal = puntuacionActual + score

                // Guardar la nueva puntuación total en la base de datos
                val datosJugador: HashMap<String, Any> = HashMap()
                datosJugador["Puntuacio"] = nuevaPuntuacionTotal.toString()
                datosJugador["Nivell"] = NIVELL // Actualizar el nivel actual del usuario

                // Actualizar la puntuación y el nivel en la base de datos
                jugadors.child(uidString).updateChildren(datosJugador)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@PokeGameScreen, "Puntuación y nivel guardados", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@PokeGameScreen, "Error al guardar la puntuación y el nivel", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }


    private fun actualizarPuntajes() {
        val uidString: String? = UID // Obtener el UID del usuario
        if (uidString != null) {
            jugadors.child(uidString).child("Puntuacion").get().addOnSuccessListener { dataSnapshot ->
                val puntaje = dataSnapshot.value.toString()
                val intent = Intent(this@PokeGameScreen, RecyclerView::class.java)
                intent.putExtra("PUNTUACIO", puntaje)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                // Manejo de errores
                Toast.makeText(this@PokeGameScreen, "Error al obtener el puntaje", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        showEndGameDialog()
    }

}
