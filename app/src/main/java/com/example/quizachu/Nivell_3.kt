package com.example.quizachu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Nivell_3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nivell3)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this@Nivell_3, seleccio_De_Nivells::class.java)
        startActivity(intent)
        finish()
    }
}