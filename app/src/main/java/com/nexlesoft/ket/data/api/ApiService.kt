package com.nexlesoft.ket.data.api

import com.nexlesoft.ket.data.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("auth/signup")
    suspend fun signUp(@Body payload: SignUpRequest): Response<SignUpResponse>

    @POST("auth/signin")
    suspend fun signIn(@Body payload: SignInRequest): Response<SignInResponse>

    @GET("categories")
    suspend fun getCategoryList(@Header("Authorization") accessToken : String) : Response<List<CategoryItem>>
}