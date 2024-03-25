package com.example.quizachu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class seleccio_De_Nivells : AppCompatActivity() {
    lateinit var nivell1: Button
    lateinit var nivell2: Button
    lateinit var nivell3: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccio_de_nivells)

        nivell1 =findViewById<Button>(R.id.btn_nivell1)
        nivell2 =findViewById<Button>(R.id.btn_nivell2)
        nivell3 =findViewById<Button>(R.id.btn_nivell3)

        nivell1.setOnClickListener(){
            val intent = Intent(this, Nivell_1::class.java)
            startActivity(intent)
            finish()
        }

        nivell2.setOnClickListener(){
            val intent = Intent(this, Nivell_2::class.java)
            startActivity(intent)
            finish()
        }

        nivell3.setOnClickListener(){
            val intent = Intent(this, Nivell_3::class.java)
            startActivity(intent)
            finish()
        }

    }

    // En cas que l'usuari vulgui tornar cap enrere se li obrirà la finestra del menú del jugador.
    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this@seleccio_De_Nivells, Menu::class.java)
        startActivity(intent)
        finish()
    }

}