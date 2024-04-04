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
    //Despleguem les variables que farem servir
    lateinit var correoLogin : EditText
    lateinit var passLogin : EditText
    lateinit var BtnLogin : Button
    lateinit var auth: FirebaseAuth //FIREBASE AUTENTIFICACIO
    lateinit var db: FirebaseDatabase
    lateinit var obrirRegistre: TextView
    lateinit var cambiaPassword: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Busquem a R els elements als que apunten les variables
        correoLogin =findViewById<EditText>(R.id.correoLogin)
        passLogin =findViewById<EditText>(R.id.passLogin)
        BtnLogin =findViewById<Button>(R.id.BtnLogin)
        obrirRegistre =findViewById<TextView>(R.id.obrirRegistre)
        cambiaPassword =findViewById<TextView>(R.id.cambiaPassword)

        obrirRegistre.setOnClickListener(){
            tancarLogin()
        }

        cambiaPassword.setOnClickListener {
            val intent = Intent(this@Login, OblidatContrasenya::class.java)
            startActivity(intent)
            finish()
        }

        BtnLogin.setOnClickListener(){
            //Abans de fer el registre validem les dades
            var email:String = correoLogin.getText().toString()
            var passw:String = passLogin.getText().toString()
            // validació del correu
            // si no es de tipus correu
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                correoLogin.setError("Invalid Mail")
            }
            else if (passw.length<6) {
                passLogin.setError("Password less than 6 chars")
            }
            else
            {
                LogindeJugador(email, passw)
            }
        }

        //Instanciem el firebaseAuth
        auth = FirebaseAuth.getInstance()
    }

    private fun LogindeJugador(email: String, passw: String) {
        auth.signInWithEmailAndPassword(email, passw)
            .addOnCompleteListener(this)
            { task ->
                if (task.isSuccessful) {
                    val tx: String = "Benvingut "+ email
                    Toast.makeText(this, tx, Toast.LENGTH_LONG).show()
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(this, "ERROR Autentificació",
                        Toast.LENGTH_LONG).show()
                }
            }
    }

    fun updateUI(user:FirebaseUser?) {
        val intent= Intent(this, Menu::class.java)
        startActivity(intent)
        finish()
    }

    private fun tancarLogin() {
        auth.signOut() //tanca la sessió
        //va a la pantalla inicial
        val intent= Intent(this, Registre::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@Login, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}