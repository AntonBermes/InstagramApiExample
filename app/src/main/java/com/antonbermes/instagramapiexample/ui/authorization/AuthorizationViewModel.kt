package com.antonbermes.instagramapiexample.ui.authorization

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.antonbermes.instagramapiexample.BuildConfig
import com.antonbermes.instagramapiexample.repository.AuthorizationRepository
import com.antonbermes.instagramapiexample.repository.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class AuthorizationViewModel(application: Application, newToken: Boolean) :
    AndroidViewModel(application) {

    private val viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val repository = AuthorizationRepository(application, newToken, coroutineScope)

    val accessToken = repository.data

    val status = repository.status

    companion object {
        private const val START_URL =
            "https://api.instagram.com/oauth/authorize/?client_id=${BuildConfig.CLIENT_ID}" +
                    "&redirect_uri=${BuildConfig.CALLBACK_URL}&response_type=code"
    }

    var currentUrl = START_URL
        private set

    fun setStatus(status: Status) {
        repository.checkStatus(status)
    }

    fun checkUrl(url: String): Boolean {
        currentUrl = url
        return repository.checkUrl(url)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}