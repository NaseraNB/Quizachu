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
    val DURACION: Long = 6000;
    var mediaPlayer: MediaPlayer? = null // Declaració de MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_splash)

        // Oculta la barra de acción
        supportActionBar?.hide()

        // Configuració del MediaPlayer amb el fitxer d'àudio
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido)

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