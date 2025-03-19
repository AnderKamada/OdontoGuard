package com.example.odontoguard.api

import com.example.odontoguard.models.FeedbackRequest
import com.example.odontoguard.models.LoginRequest
import com.example.odontoguard.models.LoginResponse
import com.example.odontoguard.models.RecompensaResponse
import com.example.odontoguard.models.RegisterRequest
import com.example.odontoguard.models.RegisterResponse
import com.example.odontoguard.models.Tarefa
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {


    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("tarefas")
    fun getTarefas(@Header("Authorization") token: String): Call<List<Tarefa>>

    @Multipart
    @POST("tarefas/enviar-foto")
    fun enviarFoto(
        @Header("Authorization") token: String,
        @Part foto: MultipartBody.Part,
        @Part("tarefaId") tarefaId: RequestBody
    ): Call<Void>


    @POST("feedback/enviar")
    fun enviarFeedback(@Header("Authorization") token: String, @Body feedback: FeedbackRequest): Call<Void>


    @GET("recompensas")
    fun getRecompensas(@Header("Authorization") token: String): Call<RecompensaResponse>

    @POST("auth/register")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>
}