package com.example.odontoguard

import android.os.Bundle
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        val token = sharedPref.getString("TOKEN", "")

        if (token.isNullOrEmpty()) {

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {

            setContentView(R.layout.activity_main)

        }
    }
}