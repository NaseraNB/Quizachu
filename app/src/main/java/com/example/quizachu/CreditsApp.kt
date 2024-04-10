package com.example.quizachu

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class CreditsApp : AppCompatActivity() {

    private lateinit var handler: Handler
    private lateinit var fragment1: Fragment
    private lateinit var fragment2: Fragment
    private var isFirstFragmentDisplayed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credits_app)

        handler = Handler()
        fragment1 = FragmentLogo()
        fragment2 = fragmentTitol()

        setupFragmentSwitch()
    }

    private fun setupFragmentSwitch() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragment1)
            .hide(fragment1)
            .commit()

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragment2)
            .hide(fragment2)
            .commit()

        handler.postDelayed(object : Runnable {
            override fun run() {
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

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@CreditsApp, Menu::class.java)
        startActivity(intent)
        finish()
    }
}
