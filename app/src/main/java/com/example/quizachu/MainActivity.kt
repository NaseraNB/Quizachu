package com.example.quizachu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    //per a comprovar si la sessió esta inicialitzada
    lateinit var auth: FirebaseAuth
    var user: FirebaseUser? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //assigna valor a user
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser

        // Variables que alamacenara los valores de las dos entradas.
        var botoLogin = findViewById<Button>(R.id.botoLogin)
        var botoRegistre = findViewById<Button>(R.id.botoRegistre)

        // Establece un listener para el evento de clic en el botón de login.
        botoLogin.setOnClickListener() {
            Toast.makeText(this, "click botó login", Toast.LENGTH_LONG).show();
            // Muestra un mensaje Toast cuando se hace clic en el botón de login.

            // Crea un Intent para cambiar a la pantalla MainActivity
            val intent = Intent(this, Login::class.java)
            // Inicia la MainActivity
            startActivity(intent)
        }

        // Establece un listener para el evento de clic en el botón de registro.
        botoRegistre.setOnClickListener() {
            Toast.makeText(this, "click botó registre", Toast.LENGTH_LONG).show();
            // Muestra un mensaje Toast cuando se hace clic en el botón de registro.

            // Crea un Intent para cambiar a la pantalla MainActivity
            val intent = Intent(this, Registre::class.java)
            // Inicia la MainActivity
            startActivity(intent)
        }



    }

    // Aquest mètode s'executarà quan s'obri el menu
    override fun onStart() {
        usuariLogejat()
        super.onStart()
    }

    private fun usuariLogejat() {
        if (user != null) {
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
            finish()
        }
    }




}