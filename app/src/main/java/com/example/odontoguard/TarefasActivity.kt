package com.example.odontoguard

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.example.odontoguard.api.ApiClient
import com.example.odontoguard.models.Tarefa
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TarefasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarefas)

        val listView: ListView = findViewById(R.id.listViewTarefas)

        val sharedPref = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        val token = sharedPref.getString("TOKEN", "") ?: ""

        if (token.isEmpty()) {
            Toast.makeText(this, "Usuário não autenticado!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        ApiClient.instance.getTarefas("Bearer $token").enqueue(object : Callback<List<Tarefa>> {
            override fun onResponse(call: Call<List<Tarefa>>, response: Response<List<Tarefa>>) {
                if (response.isSuccessful) {
                    val tarefas = response.body()?.map { it.descricao } ?: listOf("Nenhuma tarefa disponível")
                    val adapter = ArrayAdapter(this@TarefasActivity, android.R.layout.simple_list_item_1, tarefas)
                    listView.adapter = adapter
                } else {
                    Toast.makeText(this@TarefasActivity, "Erro ao buscar tarefas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Tarefa>>, t: Throwable) {
                Toast.makeText(this@TarefasActivity, "Falha: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}