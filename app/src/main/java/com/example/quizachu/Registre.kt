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

        // Busquem a R els elements als que apunten les variables
        correo = findViewById<EditText>(R.id.correo)
        pass = findViewById<EditText>(R.id.pass)
        nombre = findViewById<EditText>(R.id.nombre)
        edat = findViewById<EditText>(R.id.edatEt)
        poblacio = findViewById<EditText>(R.id.poblacioEt)
        fecha = findViewById<TextView>(R.id.fecha)
        Registrar = findViewById<Button>(R.id.Registrar)
        baseDeDades = FirebaseAuth.getInstance() //Instanciem el firebaseAuth

        //carreguem la data al TextView
        //Utilitzem calendar (hi ha moltes altres opcions)
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateInstance() //or use getDateInstance()
        val formatedDate = formatter.format(date)

        //ara la mostrem al TextView
        fecha.setText(formatedDate)

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

    fun updateUI(user:FirebaseUser?){
        //hi ha un interrogant perquè podria ser null
        if (user!=null)
        {
            var puntuacio: String = "0"
            var uidString: String = user.uid
            var correoString: String = correo.getText().toString()
            var passString: String = pass.getText().toString()
            var nombreString: String = nombre.getText().toString()
            var fechaString: String= fecha.getText().toString()
            var nivell: String = "1"
            var edatString: String = edat.getText().toString()
            var poblacioString: String = poblacio.getText().toString()

            //AQUI GUARDA EL CONTINGUT A LA BASE DE DADES
            // Utilitza un HashMap
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



            // Creem un punter a la base de dades i li donem un nom
            var database: FirebaseDatabase = FirebaseDatabase.getInstance("https://quizachu-default-rtdb.europe-west1.firebasedatabase.app")
            var reference: DatabaseReference = database.getReference("DATA BASE JUGADORS")
                if(reference!=null) {
                    Log.i("MYTAB", reference.toString())
                    Log.i("MYTAB", uidString)
                    Log.i("MYTAB", dadesJugador.toString())

                    //crea un fill amb els valors de dadesJugador
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
}