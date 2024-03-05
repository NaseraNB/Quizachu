package com.example.quizachu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.quizachu.ui.PokeGameScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Menu : AppCompatActivity() {

    //creem unes variables per comprovar ususari i authentificació
    lateinit var auth: FirebaseAuth
    var user:FirebaseUser? = null;
    lateinit var tancarSessio: Button
    lateinit var jugar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        tancarSessio =findViewById<Button>(R.id.tancarSessio)
        jugar = findViewById<Button>(R.id.jugarBtn)

        tancarSessio.setOnClickListener(){
            tancalaSessio()
        }

        jugar.setOnClickListener(){
            // Crea un Intent para cambiar a la pantalla GameScreen
            val intent = Intent(this, PokeGameScreen::class.java)
            // Inicia la GameScreen
            startActivity(intent)
        }

        auth= FirebaseAuth.getInstance()
        user =auth.currentUser

    }

    // Aquest mètode s'executarà quan s'obri el minijoc
    override fun onStart() {
        Usuarilogejat()
        super.onStart()
    }

    private fun Usuarilogejat()
    {
        if (user !=null)
        {
            Toast.makeText(this,"Jugador logejat",
                Toast.LENGTH_SHORT).show()
        }
        else
        {
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun tancalaSessio() {
        auth.signOut() //tanca la sessió
        //va a la pantalla inicial
        val intent= Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }



}