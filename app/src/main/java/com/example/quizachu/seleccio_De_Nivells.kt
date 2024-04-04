package com.example.quizachu


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.quizachu.ui.PokeGameScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class seleccio_De_Nivells : AppCompatActivity() {
    lateinit var nivell1: Button
    lateinit var nivell2: Button
    lateinit var nivell3: Button
    private var NOM: String =""
    private var PUNTUACIO: String=""
    private var UID: String=""
    private var NIVELL: String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccio_de_nivells)

        //ara recuperarem els valors
        var intent:Bundle? = getIntent().extras
        UID = intent?.get("UID").toString()
        NOM = intent?.get("NOM").toString()
        PUNTUACIO = intent?.get("PUNTUACIO").toString()
        NIVELL = intent?.get("NIVELL").toString()

        nivell1 =findViewById<Button>(R.id.btn_nivell1)
        nivell2 =findViewById<Button>(R.id.btn_nivell2)
        nivell3 =findViewById<Button>(R.id.btn_nivell3)


        nivell1.setOnClickListener(){
            val intent = Intent(this, PokeGameScreen::class.java)
            startActivity(intent)
            finish()

            if (NIVELL =="1") {
                Toast.makeText(this,"NIVELL 1",Toast.LENGTH_LONG).show()
            }

            intent.putExtra("UID", UID)
            intent.putExtra("NOM", NOM)
            intent.putExtra("PUNTUACIO", PUNTUACIO)
            intent.putExtra("NIVELL", NIVELL)
            startActivity(intent)
            finish()
        }

        nivell2.setOnClickListener(){
            val intent = Intent(this, Nivell_2::class.java)
            startActivity(intent)
            finish()

            if (NIVELL =="2") {
                Toast.makeText(this,"NIVELL 2",Toast.LENGTH_LONG).show()
            }
        }

        nivell3.setOnClickListener(){
            val intent = Intent(this, Nivell_3::class.java)
            startActivity(intent)
            finish()

            if (NIVELL =="3") {
                Toast.makeText(this,"NIVELL 1",Toast.LENGTH_LONG).show()
            }
        }
    }

    // En cas que l'usuari vulgui tornar cap enrere se li obrirà la finestra del menú del jugador.
    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this@seleccio_De_Nivells, Menu::class.java)
        startActivity(intent)
        finish()
    }

}