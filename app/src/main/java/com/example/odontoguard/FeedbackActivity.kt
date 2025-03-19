package com.example.odontoguard

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.odontoguard.api.ApiClient
import com.example.odontoguard.models.FeedbackRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedbackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        val editTextFeedback: EditText = findViewById(R.id.editTextFeedback)
        val btnEnviarFeedback: Button = findViewById(R.id.btnEnviarFeedback)
        val textViewResposta: TextView = findViewById(R.id.textViewRespostaDentista)

        btnEnviarFeedback.setOnClickListener {
            val feedback = editTextFeedback.text.toString()

            if (feedback.isNotEmpty()) {
                val sharedPref = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
                val token = sharedPref.getString("TOKEN", "") ?: ""

                val request = FeedbackRequest(feedback)
                ApiClient.instance.enviarFeedback("Bearer $token", request).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            textViewResposta.text = "Seu feedback foi enviado! O dentista responderá em breve."
                            textViewResposta.visibility = TextView.VISIBLE
                        } else {
                            Toast.makeText(this@FeedbackActivity, "Erro ao enviar feedback!", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(this@FeedbackActivity, "Falha: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Digite seu feedback!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}