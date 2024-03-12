package com.example.quizachu

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.ImageView
import com.bumptech.glide.Glide

class MainSplash : AppCompatActivity() {
    // Constante que representa la duración de la pantalla de presentación en milisegundos
    val DURACION: Long = 8000;
    var mediaPlayer: MediaPlayer? = null // Declaració de MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_splash)

        // Oculta la barra de acción
        supportActionBar?.hide()


        // Nos permite añadir un gif
        //this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        //val logo = findViewById<ImageView>(R.id.logoImage)
        //Glide.with(this).load(R.drawable.pokemon).override(2100, 4100).centerCrop().into(logo)

        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val logo = findViewById<ImageView>(R.id.logoImage)
        Glide.with(this)
            .asGif() // Indica a Glide que se trata de un GIF
            .load(R.drawable.pokemon)
            .override(1430, 2440) // Ajusta la resolución del GIF según las dimensiones de tu ImageView
            .centerCrop()
            .into(logo)



        // Configuració del MediaPlayer amb el fitxer d'àudio
        mediaPlayer = MediaPlayer.create(this, R.raw.splashmusic)

        // Reproduir àudio
        mediaPlayer?.start()

        // Llama al método para cambiar de pantalla por otra
        canviarActivity();

    }

    // Método privado para cambiar de pantalla después despues de terminar el tiempo
    private fun canviarActivity() {
        Handler().postDelayed({
            // Crea un Intent para cambiar a la pantalla MainActivity
            val intent = Intent(this, MainActivity::class.java)
            // Inicia la MainActivity
            startActivity(intent)
        }, DURACION) // Establece el tiempo de retardo antes de cambiar de pantalla.
    }
}