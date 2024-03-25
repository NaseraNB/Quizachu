package com.example.quizachu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Nivell_1 : AppCompatActivity() {
    lateinit var boton_nivel1: Button
    lateinit var puntuacio :TextView
    private var puntuacio_Num: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nivell1)

        boton_nivel1 = findViewById<Button>(R.id.boton_nivel1)
        puntuacio = findViewById<TextView>(R.id.puntuacio)

        boton_nivel1.setOnClickListener(){
            puntuacio_Num++
            puntuacio.text = "$puntuacio_Num"

            // Mostrar la puntuación en una ventana emergente
            mostrarPuntuacion(puntuacio_Num)

            // Guardar la puntuación en Firebase
            guardarPuntuacionFirebase(puntuacio_Num)

        }


    }

    private fun mostrarPuntuacion(puntuacion: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Puntuación")
        builder.setMessage("Tu puntuación es: $puntuacion")
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun guardarPuntuacionFirebase(puntuacion: Int) {
        val usuarioActual: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        usuarioActual?.let { user ->
            val database = FirebaseDatabase.getInstance()
            val referencia: DatabaseReference = database.getReference("DATA BASE JUGADORS").child(user.uid)
            referencia.child("Puntuacion").setValue(puntuacion)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@Nivell_1, seleccio_De_Nivells::class.java)
        startActivity(intent)
        finish()
    }
}