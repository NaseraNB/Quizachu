package com.example.quizachu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Nivell_1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nivell1)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this@Nivell_1, seleccio_De_Nivells::class.java)
        startActivity(intent)
        finish()
    }
}