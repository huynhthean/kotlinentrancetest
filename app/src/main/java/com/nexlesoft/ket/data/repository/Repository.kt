package com.nexlesoft.ket.data.repository

import com.nexlesoft.ket.data.api.ApiService
import com.nexlesoft.ket.data.local.LocalDataService
import com.nexlesoft.ket.data.model.*
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(
    private val localDataService: LocalDataService,
    private val apiService: ApiService
) : IRepository {

    override suspend fun signUp(payload: SignUpRequest): Response<SignUpResponse> {
        return apiService.signUp(payload)
    }

    override suspend fun signIn(payload: SignInRequest): Response<SignInResponse> {
        val response = apiService.signIn(payload)
        if (response.isSuccessful) {
            val data = response.body()!!
            localDataService.saveAccessToken(data.accessToken)
            localDataService.saveRefreshToken(data.refreshToken)
        }
        return apiService.signIn(payload)
    }

    override suspend fun getCategoryList(): Response<List<CategoryItem>> {
        val savedLocalData = localDataService.getCategoryList()
        if (savedLocalData == null || savedLocalData.isEmpty()) {
            val accessToken = localDataService.getAccessToken()
            val response = apiService.getCategoryList(accessToken)
            if (response.isSuccessful) {
                val listItem = response.body()!!
                localDataService.saveCategoryList(listItem)
            }
            return response
        }
        return Response.success(savedLocalData)
    }
}