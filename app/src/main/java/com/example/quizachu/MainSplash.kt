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
    // Constant que representa la durada de la pantalla de presentació en milisegons
    val DURACION: Long = 3000;
    var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_splash)

        // Amaga la barra d'acció
        supportActionBar?.hide()

        // Amaga la barra d'estat
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // Carrega el GIF a ImageView utilitzant la llibreria Glide
        val logo = findViewById<ImageView>(R.id.logoImage)
        Glide.with(this)
            .asGif()
            .load(R.drawable.pokemon_spl)
            .override(1000, 500)
            .centerCrop()
            .into(logo)

        // Inicialitza MediaPlayer amb el fitxer d'àudio
        mediaPlayer = MediaPlayer.create(this, R.raw.splashmusicpokemon)

        // Comença a reproduir l'àudio
        mediaPlayer?.start()

        // Crida al mètode per canviar a una altra pantalla
        canviarActivity();

    }

    // Mètode privat per canviar a una altra pantalla després de la durada de la pantalla de presentació
    private fun canviarActivity() {
        Handler().postDelayed({
            // Crea un Intent para cambiar a la pantalla MainActivity
            val intent = Intent(this, MainActivity::class.java)
            // Inicia la MainActivity
            startActivity(intent)
        }, DURACION) // Establece el tiempo de retardo antes de cambiar de pantalla.
    }
}