package com.example.quizachu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso

class DetallesJugadorActivity : AppCompatActivity() {

    // Declaració de variables per als elements de la interfície
    private lateinit var nombreTextView: TextView
    private lateinit var correoTextView: TextView
    private lateinit var edatTextView: TextView
    private lateinit var poblacionTextView: TextView
    private lateinit var puntuacioDe: TextView
    private lateinit var imagenDellates: ImageView

    // Declaració de variables per a Firebase
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null

    // Mètode onCreate, es crida quan es crea l'activitat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_jugador)

        // Inicialització dels elements de la interfície
        nombreTextView = findViewById(R.id.nombreTextView)
        correoTextView = findViewById(R.id.correoTextView)
        edatTextView = findViewById(R.id.edatTextView)
        poblacionTextView = findViewById(R.id.poblacionTextView)
        puntuacioDe = findViewById(R.id.puntuacioDe)
        imagenDellates = findViewById(R.id.imagenDellates)

        // Inicialització de Firebase
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser

        // Obtenció de dades passades a través de l'intent
        val imagen = intent.getStringExtra("Imagen")
        val nombre = intent.getStringExtra("Nom")
        val correo = intent.getStringExtra("Email")
        val puntacion = intent.getStringExtra("Puntuacio")
        val edad = intent.getStringExtra("Edat")
        val poblacion = intent.getStringExtra("Població")

        // Assignació de les dades als elements de la interfície
        nombreTextView.text = "Nombre: $nombre"
        correoTextView.text = "Correo: $correo"
        edatTextView.text = "Edad: $edad"
        poblacionTextView.text = "Población: $poblacion"
        puntuacioDe.text = "Puntación: $puntacion"


        // Carrega de la imatge amb Picasso, si no es pot carregar, carrega la imatge per defecte
        val imatge: String = imagen ?: ""
        try {
            Picasso.get().load(imatge).into(imagenDellates)
        } catch (e: Exception) {
            Picasso.get().load(R.drawable.imagen_user_pordefecto).into(imagenDellates)
        }
    }

    // Mètode que es crida quan es prem el botó de retrocés del dispositiu
    override fun onBackPressed() {
        super.onBackPressed()
        // Crea un intent per iniciar l'activitat del RecyclerView
        val intent = Intent(this@DetallesJugadorActivity, RecyclerView::class.java)
        startActivity(intent)
        finish()
    }
}
