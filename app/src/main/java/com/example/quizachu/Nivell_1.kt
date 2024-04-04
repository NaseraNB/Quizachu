package com.example.quizachu

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Nivell_1 : AppCompatActivity() {
    private var NOM: String =""
    private var PUNTUACIO: String=""
    private var UID: String=""
    private var NIVELL: String=""
    lateinit var PuntacionNivel1: TextView
    lateinit var tiempoNivell1: TextView
    lateinit var nomUser: TextView
    lateinit var NivellActual: TextView
    lateinit var buttonRespuestaCorrecta: Button

    private var contador: Int = 0

    private var fiDelJocBoolean: Boolean = false
    private lateinit var miDialog: Dialog

    lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var jugadors: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nivell1)

        PuntacionNivel1 =findViewById<TextView>(R.id.PuntacionNivel1)
        tiempoNivell1 =findViewById<TextView>(R.id.tiempoNivell1)
        nomUser =findViewById<TextView>(R.id.nomUser)
        NivellActual =findViewById<TextView>(R.id.NivellActual)
        buttonRespuestaCorrecta =findViewById<Button>(R.id.buttonRespuestaCorrecta)


        miDialog = Dialog(this)

        auth= FirebaseAuth.getInstance()
        user =auth.currentUser
        firebaseDatabase= FirebaseDatabase.getInstance()
        jugadors = firebaseDatabase.getReference("DATA BASE JUGADORS")

        var intent:Bundle? = getIntent().extras
        UID = intent?.get("UID").toString()
        NOM = intent?.get("NOM").toString()
        PUNTUACIO = intent?.get("PUNTUACIO").toString()
        NIVELL = intent?.get("NIVELL").toString()

        nomUser.text = NOM
        NivellActual.text = NIVELL
        PuntacionNivel1.text = PUNTUACIO

        // Al hacer clic al boton el contador aumentara
        buttonRespuestaCorrecta.setOnClickListener(){

            if(!fiDelJocBoolean) {
                contador++ // El contador aumenta de 1 en 1
                PuntacionNivel1.text = contador.toString() // Convertimos de int a string lo mostramos por pantalla
                missatgeFiDelJoc()
                val NombrePuntacico: TextView = miDialog.findViewById(R.id.NombrePuntacico)
                val textoPuntuacion = NombrePuntacico.text.toString()
                guardarResultats(textoPuntuacion)
            }
        }

        compteEnrere()
    }

    // Mètode que retrocedeix el temps
    private fun compteEnrere(){
        object : CountDownTimer(10000, 1000) {

            // Se ejeucta cada segundo
            override fun onTick(millisUntilFinished: Long) {
                val segundosRestantes = millisUntilFinished / 1000L // Convertir 1000 a Long
                tiempoNivell1.setText(segundosRestantes.toString())
            }

            // Cuando se acaba el tiempo
            override fun onFinish() {
                tiempoNivell1.setText("0")
                fiDelJocBoolean = true

                missatgeFiDelJoc()

                val NombrePuntacico: TextView = miDialog.findViewById(R.id.NombrePuntacico)
                val textoPuntuacion = NombrePuntacico.text.toString()
                guardarResultats(textoPuntuacion)
            }
        }.start()
    }

    private fun missatgeFiDelJoc(){
        lateinit var fiDelJoc: TextView
        lateinit var TotalDePuntuacio: TextView
        lateinit var NombrePuntacico: TextView
        lateinit var Seleccio_Nivells: Button
        lateinit var jugarDeNou: Button
        lateinit var Puntatges: Button

        miDialog.setContentView(R.layout.activity_fi_del_joc)
        fiDelJoc = miDialog.findViewById(R.id.fiDelJoc)
        TotalDePuntuacio = miDialog.findViewById(R.id.TotalDePuntuacio)
        NombrePuntacico = miDialog.findViewById(R.id.NombrePuntacico)

        Seleccio_Nivells = miDialog.findViewById(R.id.Seleccio_Nivells)
        jugarDeNou = miDialog.findViewById(R.id.jugarDeNou)
        Puntatges = miDialog.findViewById(R.id.Puntatges)

        NombrePuntacico.text = contador.toString()

        miDialog.show()

        jugarDeNou.setOnClickListener(){
            contador = 0
            miDialog.dismiss()
            PuntacionNivel1.setText("0")
            fiDelJocBoolean = false
            compteEnrere()
        }

        Seleccio_Nivells.setOnClickListener(){
            val intent = Intent(this@Nivell_1, seleccio_De_Nivells::class.java)
            startActivity(intent)
            finish()
        }

        Puntatges.setOnClickListener(){
            val intent = Intent(this@Nivell_1, RecyclerView::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun guardarResultats(puntos: String) {
        val uidString: String? = user?.uid
        if (uidString != null) {
            val dadesJugador: HashMap<String, Any> = HashMap()
            dadesJugador["Puntuacio"] = puntos

            jugadors.child(uidString).updateChildren(dadesJugador)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@Nivell_1, "Puntaje actualizado", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@Nivell_1, "Error al actualizar el puntaje", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }


    // Si l'usuari vol tornar enrere, se us obrirà el menú de selecciones de nivells
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@Nivell_1, seleccio_De_Nivells::class.java)
        startActivity(intent)
        finish()
    }
}