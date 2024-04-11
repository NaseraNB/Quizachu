package com.example.quizachu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import androidx.recyclerview.widget.RecyclerView

class RecyclerView : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var jugadoresList: MutableList<Jugadors>
    private lateinit var adapter: JugadoresAdapter
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        recyclerView = findViewById(R.id.recyclerViewPok)
        recyclerView.layoutManager = LinearLayoutManager(this)
        jugadoresList = mutableListOf()
        adapter = JugadoresAdapter(this, jugadoresList)
        recyclerView.adapter = adapter

        // Obtenir una referència a la base de dades de Firebase i llegir les dades dels jugadors
        database = FirebaseDatabase.getInstance("https://quizachu-default-rtdb.europe-west1.firebasedatabase.app").getReference("DATA BASE JUGADORS")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Esborrar la llista actual de jugadors
                jugadoresList.clear()
                // Recórrer totes les dades de l'snapshot de la base de dades
                for (dataSnapshot in snapshot.children) {

                    // Convertir les dades de l'snapshot en un objecte Jugadors i afegir-lo a la llista
                    val jugador = dataSnapshot.getValue(Jugadors::class.java)
                    jugador?.let { jugadoresList.add(it) }
                }
                // Ordenar la llista de jugadors segons la puntuació de major a menor
                jugadoresList.sortByDescending { it.Puntuacio.toDoubleOrNull() ?: 0.0 }
                // Notificar al adaptador que les dades han canviat
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar el error de lectura de la base de datos aquí
            }
        })
    }

    // Maneja la pressió del botó de retrocés per tornar al menú principal
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@RecyclerView, Menu::class.java)
        startActivity(intent)
        finish()
    }
}