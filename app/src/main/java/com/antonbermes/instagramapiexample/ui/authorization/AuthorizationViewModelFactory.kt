package com.antonbermes.instagramapiexample.ui.authorization

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AuthorizationViewModelFactory(
    private val application: Application,
    private val isNewToken: Boolean
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthorizationViewModel::class.java)) {
            return AuthorizationViewModel(application, isNewToken) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}