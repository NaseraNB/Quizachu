package com.example.quizachu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth

class OblidatContrasenya : AppCompatActivity() {

    lateinit var correoRecuperar : EditText
    lateinit var BtnRecuperar : Button
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oblidat_contrasenya)

        correoRecuperar = findViewById<EditText>(R.id.correoRecuperar)
        BtnRecuperar = findViewById<Button>(R.id.BtnRecuperar)

        BtnRecuperar.setOnClickListener {
            validar()
        }

        auth = FirebaseAuth.getInstance()
    }

    private fun validar() {
        val email = correoRecuperar.text.toString().trim()

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            correoRecuperar.error = "Correo inválido"
            return
        }

        enviaCorreuElectronic(email)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this@OblidatContrasenya, Login::class.java)
        startActivity(intent)
        finish()
    }

    private fun enviaCorreuElectronic(email: String) {
        val emailAddress = email

        auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this@OblidatContrasenya, "Correo enviado", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@OblidatContrasenya, Login::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@OblidatContrasenya, "Correo inválido", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
