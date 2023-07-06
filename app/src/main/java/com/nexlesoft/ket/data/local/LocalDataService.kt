package com.nexlesoft.ket.data.local

import android.content.Context
import android.util.Log
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nexlesoft.ket.data.model.CategoryItem
import java.lang.reflect.Type
import javax.inject.Inject


class LocalDataService @Inject constructor(
    private val context: Context
) {

    private val TAG = "LocalDataService"

    fun saveAccessToken(accessToken: String) {
        Log.d(TAG, "saveAccessToken : $accessToken")
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit().putString(PREF_KEY_ACCESS_TOKEN, accessToken).apply()
    }

    fun getAccessToken(): String {
        val accessToken = PreferenceManager.getDefaultSharedPreferences(context)
            .getString(PREF_KEY_ACCESS_TOKEN, "")!!
        Log.d(TAG, "getAccessToken : $accessToken")
        return "Bearer $accessToken"
    }

    fun saveRefreshToken(refreshToken: String) {
        Log.d(TAG, "saveRefreshToken : $refreshToken")
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit().putString(PREF_KEY_REFRESH_TOKEN, refreshToken).apply()
    }

    fun saveCategoryList(listItem: List<CategoryItem>) {
        val jsonData = Gson().toJson(listItem)
        Log.d(TAG, "saveCategoryList : $jsonData")
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit().putString(PREF_KEY_CATEGORY_LIST, jsonData).apply()
    }

    fun getCategoryList() : List<CategoryItem>?{
        val type: Type = object : TypeToken<List<CategoryItem?>?>() {}.type
        val data = PreferenceManager.getDefaultSharedPreferences(context)
            .getString(PREF_KEY_CATEGORY_LIST, "")!!
        Log.d(TAG, "getAccessToken : $data")
        return try {
            Gson().fromJson(data, type)
        } catch (ex : Exception) {
            null
        }
    }

    fun getRefreshToken(): String {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getString(PREF_KEY_REFRESH_TOKEN, "")!!
    }

    companion object {
        private const val PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN"
        private const val PREF_KEY_REFRESH_TOKEN = "PREF_KEY_REFRESH_TOKEN"
        private const val PREF_KEY_CATEGORY_LIST = "PREF_KEY_CATEGORY_LIST"
    }
}