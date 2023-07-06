package com.nexlesoft.ket.data.model

data class SignUpResponse(
    val id: Int,
    val createAt: String,
    val updateAt: String,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val role: String
)