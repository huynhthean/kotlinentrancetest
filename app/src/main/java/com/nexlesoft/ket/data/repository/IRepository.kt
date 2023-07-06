package com.nexlesoft.ket.data.repository

import com.nexlesoft.ket.data.model.*
import retrofit2.Response


interface IRepository {
    suspend fun signUp(payload: SignUpRequest): Response<SignUpResponse>

    suspend fun signIn(payload: SignInRequest): Response<SignInResponse>

    suspend fun getCategoryList(): Response<List<CategoryItem>>
}