package com.example.quizachu

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Menu : AppCompatActivity() {

    //creem unes variables per comprovar ususari i authentificació
    lateinit var auth: FirebaseAuth
    var user: FirebaseUser? = null;
    lateinit var tancarSessio: Button
    lateinit var CreditsBtn: Button
    lateinit var PuntuacionsBtn: Button
    lateinit var jugarBtn: Button
    //reference serà el punter que ens envia a la base de dades de jugadors
    lateinit var reference: DatabaseReference

    lateinit var puntuacio: TextView
    lateinit var uid: TextView
    lateinit var correo: TextView
    lateinit var nom: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        //Aquí creem un tipus de lletra a partir de una font
        val tf = Typeface.createFromAsset(assets,"Komigo3D-Regular.ttf")

        tancarSessio =findViewById<Button>(R.id.tancarSessio)
        CreditsBtn =findViewById<Button>(R.id.CreditsBtn)
        PuntuacionsBtn =findViewById<Button>(R.id.PuntuacionsBtn)
        jugarBtn =findViewById<Button>(R.id.jugarBtn)

        puntuacio =findViewById(R.id.puntuacio)
        uid =findViewById(R.id.uid)
        correo =findViewById(R.id.correo)
        nom =findViewById(R.id.nom)

        tancarSessio.setTypeface(tf)
        CreditsBtn.setTypeface(tf)
        PuntuacionsBtn.setTypeface(tf)
        jugarBtn.setTypeface(tf)
        consulta()

        tancarSessio.setOnClickListener(){
            tancalaSessio()
        }

        CreditsBtn.setOnClickListener(){
            Toast.makeText(this,"Credits", Toast.LENGTH_SHORT).show()
        }
        PuntuacionsBtn.setOnClickListener(){
            Toast.makeText(this,"Puntuacions", Toast.LENGTH_SHORT).show()
        }
        jugarBtn.setOnClickListener(){
            Toast.makeText(this,"JUGAR", Toast.LENGTH_SHORT).show()
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

    private fun consulta(){
        var database: FirebaseDatabase = FirebaseDatabase.getInstance("https://quizachu-default-rtdb.europe-west1.firebasedatabase.app/")
        var bdreference:DatabaseReference = database.getReference("DATA BASE JUGADORS")
        bdreference.addValueEventListener (object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i ("DEBUG","arrel value"+ snapshot.getValue().toString())
                Log.i ("DEBUG","arrel key"+ snapshot.key.toString())
                // ara capturem tots els fills
                var trobat:Boolean =false
                for (ds in snapshot.getChildren()) {
                    Log.i ("DEBUG","DS key "+ds.child("Uid").key.toString())
                    Log.i ("DEBUG","DS value: "+ds.child("Uid").getValue().toString())
                    Log.i ("DEBUG","DS data:"+ds.child("Data").getValue().toString())
                    Log.i ("DEBUG","DS mail:"+ds.child("Email").getValue().toString())
                    //mirem si el mail és el mateix que el del jugador
                    //si és així, mostrem les dades als textview corresponents
                    Log.i("USER", user?.email.toString())
                        if (ds.child("Email").getValue().toString().equals(user?.email)){
                            trobat=true
                            //carrega els textview
                            puntuacio.setText(ds.child("Puntuacio").getValue().toString())
                            uid.setText(ds.child("Uid").getValue().toString())
                            correo.setText(ds.child("Email").getValue().toString())
                            nom.setText(ds.child("Nom").getValue().toString())
                        }
                        if (!trobat)
                        {
                            Log.e ("ERROR","ERROR NO TROBAT MAIL")
                        }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e ("ERROR","ERROR DATABASE CANCEL")
            }
        })
    }
}