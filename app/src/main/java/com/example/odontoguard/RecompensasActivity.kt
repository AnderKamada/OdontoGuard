package com.example.odontoguard

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.odontoguard.api.ApiClient
import com.example.odontoguard.models.RecompensaResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecompensasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recompensas)

        val textViewPontos: TextView = findViewById(R.id.textViewPontos)
        val listViewRecompensas: ListView = findViewById(R.id.listViewRecompensas)

        val sharedPref = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        val token = sharedPref.getString("TOKEN", "") ?: ""

        if (token.isEmpty()) {
            Toast.makeText(this, "Usuário não autenticado!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        ApiClient.instance.getRecompensas("Bearer $token").enqueue(object : Callback<RecompensaResponse> {
            override fun onResponse(call: Call<RecompensaResponse>, response: Response<RecompensaResponse>) {
                if (response.isSuccessful) {
                    val recompensaResponse = response.body()
                    if (recompensaResponse != null) {
                        val pontos = recompensaResponse.pontos
                        val recompensas = recompensaResponse.recompensas

                        textViewPontos.text = "Seus pontos: $pontos"

                        val adapter = ArrayAdapter(this@RecompensasActivity, android.R.layout.simple_list_item_1, recompensas)
                        listViewRecompensas.adapter = adapter
                    } else {
                        Toast.makeText(this@RecompensasActivity, "Resposta vazia", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@RecompensasActivity, "Erro ao buscar recompensas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RecompensaResponse>, t: Throwable) {
                Toast.makeText(this@RecompensasActivity, "Falha: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}