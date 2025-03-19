package com.example.odontoguard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.odontoguard.api.ApiClient
import com.example.odontoguard.models.RegisterRequest
import com.example.odontoguard.models.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val editTextConfirmPassword = findViewById<EditText>(R.id.editTextConfirmPassword)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)
        val textViewError = findViewById<TextView>(R.id.textViewError)

        buttonRegister.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            val confirmPassword = editTextConfirmPassword.text.toString()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                textViewError.text = "Preencha todos os campos!"
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                textViewError.text = "As senhas não coincidem!"
                return@setOnClickListener
            }

            val request = RegisterRequest(email, password)

            ApiClient.instance.register(request).enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    if (response.isSuccessful) {
                        val registerResponse = response.body()
                        if (registerResponse != null) {
                            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            textViewError.text = "Resposta vazia!"
                        }
                    } else {
                        textViewError.text = "Erro de cadastro!"
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    textViewError.text = "Falha: ${t.message}"
                }
            })
        }
    }
}