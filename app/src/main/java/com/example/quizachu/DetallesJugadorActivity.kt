package com.example.quizachu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class DetallesJugadorActivity : AppCompatActivity() {

    private lateinit var nombreTextView: TextView
    private lateinit var correoTextView: TextView
    private lateinit var edatTextView: TextView
    private lateinit var poblacionTextView: TextView
    private lateinit var puntuacioDe: TextView
    private lateinit var imagenDellates: ImageView

    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_jugador)

        nombreTextView = findViewById(R.id.nombreTextView)
        correoTextView = findViewById(R.id.correoTextView)
        edatTextView = findViewById(R.id.edatTextView)
        poblacionTextView = findViewById(R.id.poblacionTextView)
        puntuacioDe = findViewById(R.id.puntuacioDe)
        imagenDellates = findViewById(R.id.imagenDellates)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser

        val imagen = intent.getStringExtra("Imagen")
        val nombre = intent.getStringExtra("Nom")
        val correo = intent.getStringExtra("Email")
        val puntacion = intent.getStringExtra("Puntuacio")
        val edad = intent.getStringExtra("Edat")
        val poblacion = intent.getStringExtra("Població")

        nombreTextView.text = "Nombre: $nombre"
        correoTextView.text = "Correo: $correo"
        edatTextView.text = "Edad: $edad"
        poblacionTextView.text = "Población: $poblacion"
        puntuacioDe.text = "Puntación: $puntacion"

        val imatge: String = imagen ?: ""
        try {
            Picasso.get().load(imatge).into(imagenDellates)
        } catch (e: Exception) {
            Picasso.get().load(R.drawable.imagen_user_pordefecto).into(imagenDellates)
        }
    }
}
