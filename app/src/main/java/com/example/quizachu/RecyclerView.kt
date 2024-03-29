package com.example.quizachu

import JugadoresAdapter
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
        adapter = JugadoresAdapter(jugadoresList)
        recyclerView.adapter = adapter

        database = FirebaseDatabase.getInstance("https://quizachu-default-rtdb.europe-west1.firebasedatabase.app").getReference("DATA BASE JUGADORS")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                jugadoresList.clear()
                for (dataSnapshot in snapshot.children) {
                    val jugador = dataSnapshot.getValue(Jugadors::class.java)
                    jugador?.let { jugadoresList.add(it) }
                }
                jugadoresList.sortByDescending { it.Puntuacio.toDoubleOrNull() ?: 0.0 }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar el error de lectura de la base de datos aquí
            }
        })
    }
}