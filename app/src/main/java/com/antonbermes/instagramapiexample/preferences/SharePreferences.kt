package com.antonbermes.instagramapiexample.preferences

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class SharedPreferences(application: Application) {

    companion object {
        private const val MY_SETTINGS = "mySettings"
        private const val TOKEN = "token"
    }

    private var sharedPreferences: SharedPreferences = application.getSharedPreferences(MY_SETTINGS, Context.MODE_PRIVATE)

    fun getToken(): String? {
        return sharedPreferences.getString(TOKEN, null)
    }

    fun saveToken(token: String) {
        sharedPreferences.edit().putString(TOKEN, token).apply()
    }

    fun deleteToken() {
        sharedPreferences.edit().remove(TOKEN).apply()
    }
}