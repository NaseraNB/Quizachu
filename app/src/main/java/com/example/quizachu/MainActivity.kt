package com.example.quizachu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    // Variables per comprovar si la sessió està iniciada
    lateinit var auth: FirebaseAuth
    var user: FirebaseUser? = null;

    // Mètode onCreate, es crida quan es crea l'activitat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicialització de Firebase Auth
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser

        // Referència als botons de login i registre
        var botoLogin = findViewById<Button>(R.id.botoLogin)
        var botoRegistre = findViewById<Button>(R.id.botoRegistre)

        // Establiment del listener per al clic al botó de login
        botoLogin.setOnClickListener() {
            // Creació d'un Intent per canviar a l'activitat de Login
            val intent = Intent(this, Login::class.java)
            // Inici de l'activitat de Login
            startActivity(intent)
        }

        // Establiment del listener per al clic al botó de registre
        botoRegistre.setOnClickListener() {
            // Creació d'un Intent per canviar a l'activitat de Registre
            val intent = Intent(this, Registre::class.java)
            // Inici de l'activitat de Registre
            startActivity(intent)
        }
    }

    // Mètode onStart, es crida quan s'inicia l'activitat
    override fun onStart() {
        usuariLogejat()
        super.onStart()
    }

    // Mètode per comprovar si l'usuari està logejat
    private fun usuariLogejat() {
        if (user != null) {
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Canvia d'activitat
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@MainActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}