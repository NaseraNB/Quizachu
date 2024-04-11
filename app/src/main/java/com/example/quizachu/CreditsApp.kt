package com.example.quizachu

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


// Credits
class CreditsApp : AppCompatActivity() {

    // Declaració de variables
    private lateinit var handler: Handler
    private lateinit var fragment1: Fragment
    private lateinit var fragment2: Fragment
    private var isFirstFragmentDisplayed = true

    // Mètode onCreate, es crida quan es crea l'activitat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credits_app)

        // Inicialització de les variables
        handler = Handler()
        fragment1 = FragmentLogo()
        fragment2 = fragmentTitol()

        // Configuració del canvi de fragments
        setupFragmentSwitch()
    }

    // Mètode per configurar el canvi de fragments
    private fun setupFragmentSwitch() {

        // Amaga el primer fragment inicialment
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragment1)
            .hide(fragment1)
            .commit()

        // Amaga el segon fragment inicialment
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragment2)
            .hide(fragment2)
            .commit()

        // Programa el canvi de fragment cada cert temps
        handler.postDelayed(object : Runnable {
            override fun run() {
                // Alterna entre mostrar el primer i el segon fragment
                if (isFirstFragmentDisplayed) {
                    supportFragmentManager.beginTransaction()
                        .hide(fragment2)
                        .show(fragment1)
                        .commit()
                } else {
                    supportFragmentManager.beginTransaction()
                        .hide(fragment1)
                        .show(fragment2)
                        .commit()
                }
                isFirstFragmentDisplayed = !isFirstFragmentDisplayed
                handler.postDelayed(this, 3000)
            }
        },1000)
    }

    // Mètode que es crida quan es prem el botó de retrocés del dispositiu
    override fun onBackPressed() {
        super.onBackPressed()
        // Crea un intent per iniciar l'activitat del menú principal
        val intent = Intent(this@CreditsApp, Menu::class.java)
        startActivity(intent)
        finish()
    }
}
