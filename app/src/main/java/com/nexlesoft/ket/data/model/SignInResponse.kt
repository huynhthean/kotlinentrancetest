package com.nexlesoft.ket.data.model

data class SignInResponse(
    val user: User,
    val accessToken: String,
    val refreshToken: String,
)