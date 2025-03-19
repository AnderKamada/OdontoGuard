package com.example.odontoguard.models

// Modelo de Login
data class LoginRequest(val email: String, val senha: String)
data class LoginResponse(val token: String, val userId: String)

// Modelo de Tarefa
data class Tarefa(val id: Int, val descricao: String, val status: String)

// Modelo de Feedback
data class FeedbackRequest(val mensagem: String)
data class ResponseMessage(val mensagem: String)
