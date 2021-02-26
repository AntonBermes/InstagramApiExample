package com.antonbermes.instagramapiexample.repository

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.antonbermes.instagramapiexample.BuildConfig
import com.antonbermes.instagramapiexample.network.InstaApi
import com.antonbermes.instagramapiexample.preferences.SharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AuthorizationRepository(
    application: Application,
    newToken: Boolean,
    private val coroutineScope: CoroutineScope
) : RepositoryInterface<String> {

    private val _data = MutableLiveData<String>()
    override val data: LiveData<String>
        get() = _data

    private val _status = MutableLiveData<Status>()
    override val status: LiveData<Status>
        get() = _status

    private var isRequestToken = false

    private val sharedPreferences = SharedPreferences(application)

    init {
        if (newToken) sharedPreferences.deleteToken()
        else _data.value = sharedPreferences.getToken()
    }

    companion object {
        private const val QUERY_PARAMETER = "code"
    }

    private fun getToken(code: String) {
        coroutineScope.launch {
            isRequestToken = true
            val getAccessTokenDeferred = InstaApi.RETROFIT_SERVICE.getAccessTokenAsync(code)
            try {
                val token = getAccessTokenDeferred.await().accessToken
                val longLivedToken = InstaApi.RETROFIT_SERVICE.getLongLivedAccessTokenAsync(token)
                    .await().accessToken
                sharedPreferences.saveToken(longLivedToken)
                _data.value = longLivedToken
            } catch (e: Exception) {
                _status.value = Status.Error(e.toString())
            }
            isRequestToken = false
        }
    }

    fun checkUrl(url: String): Boolean {
        if (isRequestToken) return true
        if (url.startsWith(BuildConfig.CALLBACK_URL)) {
            val code = Uri.parse(url).getQueryParameter(QUERY_PARAMETER)?.removeSuffix("#_")
            if (code != null) getToken(code)
        }
        return false
    }

    fun checkStatus(status: Status) {
        if (!isRequestToken) _status.value = status
    }
}