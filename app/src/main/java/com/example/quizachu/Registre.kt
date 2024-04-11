package com.example.quizachu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar

class Registre : AppCompatActivity() {
    //Definim les variables que farem servir
    //lateinit ens permet no inicialitzar-les encara
    lateinit var correo :EditText
    lateinit var pass :EditText
    lateinit var nombre :EditText
    lateinit var edat :EditText
    lateinit var poblacio :EditText
    lateinit var fecha :TextView
    lateinit var Registrar : Button
    lateinit var baseDeDades: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registre)

        // Assigna els elements de l'interfície als objectes
        correo = findViewById<EditText>(R.id.correo)
        pass = findViewById<EditText>(R.id.pass)
        nombre = findViewById<EditText>(R.id.nombre)
        edat = findViewById<EditText>(R.id.edatEt)
        poblacio = findViewById<EditText>(R.id.poblacioEt)
        fecha = findViewById<TextView>(R.id.fecha)
        Registrar = findViewById<Button>(R.id.Registrar)
        baseDeDades = FirebaseAuth.getInstance()

        // Obtenir la data actual i mostrar-la a la interfície d'usuari
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateInstance() //or use getDateInstance()
        val formatedDate = formatter.format(date)

        //ara la mostrem al TextView
        fecha.setText(formatedDate)

        // Defineix l'acció del botó de registre
        Registrar.setOnClickListener() {
            //Abans de fer el registre validem les dades
            var email: String = correo.getText().toString()
            var passE: String = pass.getText().toString()

            // validació del correu
            // si no es de tipus correu
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                correo.setError("Invalid Mail")
            } else if (passE.length < 6) {
                pass.setError("Password less than 6 chars")
            } else {
                RegistrarJugador(email, passE)
            }
        }
    }

    // Funció per registrar un nou jugador a Firebase Authentication
    fun RegistrarJugador(email:String, passw:String){
        baseDeDades.createUserWithEmailAndPassword(email, passw)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,"createUserWithEmail:success",Toast.LENGTH_SHORT).show()
                    val user = baseDeDades.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }
            }
    }

    // Actualitza la interfície d'usuari després del registre
    fun updateUI(user:FirebaseUser?){
        //hi ha un interrogant perquè podria ser null
        if (user!=null)
        {
            // Recupera les dades del formulari
            var puntuacio: String = "0"
            var uidString: String = user.uid
            var correoString: String = correo.getText().toString()
            var passString: String = pass.getText().toString()
            var nombreString: String = nombre.getText().toString()
            var fechaString: String= fecha.getText().toString()
            var nivell: String = "1"
            var edatString: String = edat.getText().toString()
            var poblacioString: String = poblacio.getText().toString()

            // Guarda les dades del jugador a Firebase Realtime Database
            var dadesJugador : HashMap<String,String> = HashMap<String, String>()
            dadesJugador.put ("Uid",uidString)
            dadesJugador.put ("Email",correoString)
            dadesJugador.put ("Password",passString)
            dadesJugador.put ("Nom",nombreString)
            dadesJugador.put ("Edat",edatString)
            dadesJugador.put ("Població", poblacioString)
            dadesJugador.put ("Imatge","")
            dadesJugador.put ("Data",fechaString)
            dadesJugador.put ("Puntuacio",puntuacio)
            dadesJugador.put ("Nivell", nivell)


            // Accedeix a Firebase Realtime Database i guarda les dades del jugador
            var database: FirebaseDatabase = FirebaseDatabase.getInstance("https://quizachu-default-rtdb.europe-west1.firebasedatabase.app")
            var reference: DatabaseReference = database.getReference("DATA BASE JUGADORS")
                if(reference!=null) {
                    Log.i("MYTAB", reference.toString())
                    Log.i("MYTAB", uidString)
                    Log.i("MYTAB", dadesJugador.toString())

                    // Crea un fill amb les dades del jugador
                    reference.child(uidString).setValue(dadesJugador)
                    Toast.makeText(this, "USUARI BEN REGISTRAT", Toast.LENGTH_SHORT).show()
                    val intent= Intent(this, Menu::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this, "ERROR BD", Toast.LENGTH_SHORT).show()
                }
                finish()
        }
        else
        {
            Toast.makeText( this,"ERROR CREATE USER",Toast.LENGTH_SHORT).show()
        }
    }
    // Canvia d'activitat
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@Registre, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}