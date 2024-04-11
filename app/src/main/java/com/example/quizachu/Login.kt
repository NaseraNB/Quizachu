package com.example.quizachu

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class Login : AppCompatActivity() {
    // Declaració de variables
    lateinit var correoLogin: EditText
    lateinit var passLogin: EditText
    lateinit var BtnLogin: Button
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseDatabase
    lateinit var obrirRegistre: TextView
    lateinit var cambiaPassword: TextView

    // Mètode onCreate, es crida quan es crea l'activitat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Referències als elements de la interfície
        correoLogin = findViewById<EditText>(R.id.correoLogin)
        passLogin = findViewById<EditText>(R.id.passLogin)
        BtnLogin = findViewById<Button>(R.id.BtnLogin)
        obrirRegistre = findViewById<TextView>(R.id.obrirRegistre)
        cambiaPassword = findViewById<TextView>(R.id.cambiaPassword)

        // Acció que es realitza al clicar a "Registra't"
        obrirRegistre.setOnClickListener() {
            tancarLogin()
        }

        // Acció que es realitza al clicar a "Has oblidat la contrasenya?"
        cambiaPassword.setOnClickListener {
            val intent = Intent(this@Login, OblidatContrasenya::class.java)
            startActivity(intent)
            finish()
        }

        // Acció que es realitza al clicar al botó de login
        BtnLogin.setOnClickListener() {
            // Validació dels camps d'entrada
            var email: String = correoLogin.getText().toString()
            var passw: String = passLogin.getText().toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                correoLogin.setError("Invalid Mail")
            } else if (passw.length < 6) {
                passLogin.setError("Password less than 6 chars")
            } else {
                LogindeJugador(email, passw)
            }
        }

        // Inicialització de FirebaseAuth
        auth = FirebaseAuth.getInstance()
    }

    // Mètode per a l'autenticació del jugador
    private fun LogindeJugador(email: String, passw: String) {
        auth.signInWithEmailAndPassword(email, passw)
            .addOnCompleteListener(this)
            { task ->
                if (task.isSuccessful) {
                    val tx: String = "Benvingut " + email
                    Toast.makeText(this, tx, Toast.LENGTH_LONG).show()
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(
                        this, "ERROR Autentificació",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    // Mètode per a actualitzar la interfície d'usuari després de l'autenticació
    fun updateUI(user: FirebaseUser?) {
        val intent = Intent(this, Menu::class.java)
        startActivity(intent)
        finish()
    }

    // Mètode per a tancar la sessió de login
    private fun tancarLogin() {
        auth.signOut() //tanca la sessió
        // Va a la pantalla d'inici de registre
        val intent = Intent(this, Registre::class.java)
        startActivity(intent)
        finish()
    }

    // Canvia d'activitat
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@Login, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}