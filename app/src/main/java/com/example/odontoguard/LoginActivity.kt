package com.example.odontoguard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.odontoguard.api.ApiClient
import com.example.odontoguard.models.LoginRequest
import com.example.odontoguard.models.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val textViewError = findViewById<TextView>(R.id.textViewError)
        val textViewRegister = findViewById<TextView>(R.id.textViewRegister)

        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                textViewError.text = "Preencha todos os campos!"
                return@setOnClickListener
            }

            val request = LoginRequest(email, password)

            ApiClient.instance.login(request).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse != null) {
                            val token = loginResponse.token

                            val sharedPref = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
                            with(sharedPref.edit()) {
                                putString("TOKEN", token)
                                apply()
                            }

                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish() // Fecha a LoginActivity
                        } else {
                            textViewError.text = "Resposta vazia!"
                        }
                    } else {
                        textViewError.text = "Erro de login!"
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    textViewError.text = "Falha: ${t.message}"
                }
            })
        }

        textViewRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}