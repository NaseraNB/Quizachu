package com.example.quizachu

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.rpc.context.AttributeContext.Auth
import java.util.Objects
import com.google.firebase.auth.GoogleAuthProvider


class Login : AppCompatActivity() {
    //Despleguem les variables que farem servir
    lateinit var correoLogin : EditText
    lateinit var passLogin : EditText
    lateinit var BtnLogin : Button
    lateinit var auth: FirebaseAuth //FIREBASE AUTENTIFICACIO
    lateinit var BtnGoogle : Button
    lateinit var db: FirebaseDatabase
    lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 123 // Asigna el valor que desees
    lateinit var obrirRegistre: TextView
    lateinit var  cambiaPassword: TextView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Busquem a R els elements als que apunten les variables
        correoLogin =findViewById<EditText>(R.id.correoLogin)
        passLogin =findViewById<EditText>(R.id.passLogin)
        BtnLogin =findViewById<Button>(R.id.BtnLogin)
        BtnGoogle =findViewById<Button>(R.id.googleButton)
        obrirRegistre =findViewById<TextView>(R.id.obrirRegistre)
        cambiaPassword =findViewById<TextView>(R.id.cambiaPassword)


        obrirRegistre.setOnClickListener(){
            tancarLogin()
        }

        cambiaPassword.setOnClickListener {
            val intent = Intent(this@Login, OblidatContrasenya::class.java)
            startActivity(intent)
            finish()
        }

        BtnLogin.setOnClickListener(){
            //Abans de fer el registre validem les dades
            var email:String = correoLogin.getText().toString()
            var passw:String = passLogin.getText().toString()
            // validació del correu
            // si no es de tipus correu
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                correoLogin.setError("Invalid Mail")
            }
            else if (passw.length<6) {
                passLogin.setError("Password less than 6 chars")
            }
            else
            {
                LogindeJugador(email, passw)
            }
        }


        //Instanciem el firebaseAuth
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)

        BtnGoogle.setOnClickListener {
            googleSignIn()
        }

    }

    private fun googleSignIn() {
        val intent = googleSignInClient.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            handleGoogleSignInResult(data)
        }
    }

    private fun handleGoogleSignInResult(data: Intent?) {
        val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuth(account?.idToken)
        } catch (e: ApiException) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun firebaseAuth(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val map = HashMap<String, Any>()
                    map["id"] = user?.uid ?: ""
                    map["name"] = user?.displayName ?: ""

                    db.reference.child("users").child(user?.uid ?: "").setValue(map)
                        .addOnCompleteListener { innerTask ->
                            if (innerTask.isSuccessful) {
                                val intent = Intent(this@Login, Menu::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this@Login, "Something went wrong", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this@Login, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }



    private fun LogindeJugador(email: String, passw: String) {
        auth.signInWithEmailAndPassword(email, passw)
            .addOnCompleteListener(this)
            { task ->
                if (task.isSuccessful) {
                    val tx: String = "Benvingut "+ email
                    Toast.makeText(this, tx, Toast.LENGTH_LONG).show()
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(this, "ERROR Autentificació",
                        Toast.LENGTH_LONG).show()
                }
            }
    }
    fun updateUI(user:FirebaseUser?)
    {
        val intent= Intent(this, Menu::class.java)
        startActivity(intent)
        finish()
    }


    private fun tancarLogin() {
        auth.signOut() //tanca la sessió
        //va a la pantalla inicial
        val intent= Intent(this, Registre::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@Login, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}