package com.example.odontoguard

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.odontoguard.api.ApiClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class EnvioFotoActivity : AppCompatActivity() {

    private lateinit var imageViewFoto: ImageView
    private lateinit var btnEnviarFoto: Button
    private var fotoBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_envio_foto)

        val btnCapturarFoto: Button = findViewById(R.id.btnCapturarFoto)
        imageViewFoto = findViewById(R.id.imageViewFoto)
        btnEnviarFoto = findViewById(R.id.btnEnviarFoto)

        btnCapturarFoto.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 1)
        }

        btnEnviarFoto.setOnClickListener {
            fotoBitmap?.let { bitmap ->
                enviarFoto(bitmap)
            } ?: Toast.makeText(this, "Capture uma foto primeiro!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            fotoBitmap = data?.extras?.get("data") as Bitmap
            imageViewFoto.setImageBitmap(fotoBitmap)
            imageViewFoto.visibility = ImageView.VISIBLE
            btnEnviarFoto.visibility = Button.VISIBLE
        }
    }

    private fun enviarFoto(bitmap: Bitmap) {
        val sharedPref = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        val token = sharedPref.getString("TOKEN", "") ?: ""

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()

        val requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), byteArray)
        val fotoPart = MultipartBody.Part.createFormData("foto", "tarefa.jpg", requestBody)
        val tarefaId = RequestBody.create("text/plain".toMediaTypeOrNull(), "1") // ID da tarefa

        ApiClient.instance.enviarFoto("Bearer $token", fotoPart, tarefaId)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@EnvioFotoActivity, "Foto enviada com sucesso!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@EnvioFotoActivity, "Erro ao enviar foto!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@EnvioFotoActivity, "Falha: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}