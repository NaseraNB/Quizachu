package com.example.quizachu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.quizachu.ui.PokeGameScreen

class seleccio_De_Nivells : AppCompatActivity() {
    lateinit var nivell1: Button
    lateinit var nivell2: Button
    lateinit var nivell3: Button
    private var NOM: String = ""
    private var PUNTUACIO: String = ""
    private var UID: String = ""
    private var NIVELL: String = "0" // Establecer el nivel predeterminado en 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccio_de_nivells)

        // Recuperar valores del Intent
        val intent: Bundle? = getIntent().extras
        UID = intent?.getString("UID").toString()
        NOM = intent?.getString("NOM").toString()
        PUNTUACIO = intent?.getString("PUNTUACIO").toString()
        NIVELL = intent?.getString("NIVELL").toString()

        // Inicializar botones
        nivell1 = findViewById<Button>(R.id.btn_nivell1)
        nivell2 = findViewById<Button>(R.id.btn_nivell2)
        nivell3 = findViewById<Button>(R.id.btn_nivell3)

        // Deshabilitar botones según el nivel actual
        nivell1.setOnClickListener {
            NIVELL = "1"
            iniciarJuego()
            nivell2.isEnabled = true
        }

        nivell2.setOnClickListener {
            if (NIVELL.toInt() >= 1) {
                NIVELL = "2"
                iniciarJuego()
            } else {
                Toast.makeText(this, "Completa el nivel 1 primero", Toast.LENGTH_SHORT).show()
            }
            nivell3.isEnabled = true
        }

        nivell3.setOnClickListener {
            if (NIVELL.toInt() >= 2) {
                NIVELL = "3"
                iniciarJuego()
            } else {
                Toast.makeText(this, "Completa el nivel 2 primero", Toast.LENGTH_SHORT).show()
            }
        }

        // Deshabilitar botones según el nivel actual
        nivell2.isEnabled = NIVELL.toInt() >= 1
        nivell3.isEnabled = NIVELL.toInt() >= 2
    }

    private fun iniciarJuego() {
        // Iniciar la actividad del juego
        val intent = Intent(this, PokeGameScreen::class.java)
        intent.putExtra("UID", UID)
        intent.putExtra("NOM", NOM)
        intent.putExtra("PUNTUACIO", PUNTUACIO)
        intent.putExtra("NIVELL", NIVELL)
        startActivity(intent)
        finish()
    }





    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this@seleccio_De_Nivells, Menu::class.java)
        startActivity(intent)
        finish()
    }
}
