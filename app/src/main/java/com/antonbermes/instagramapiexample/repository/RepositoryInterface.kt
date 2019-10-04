package com.antonbermes.instagramapiexample.repository

import androidx.lifecycle.LiveData

interface RepositoryInterface<T> {
    val data: LiveData<T>
    val status: LiveData<Status>
}

sealed class Status {
    object StartLoading : Status()
    object StopLoading : Status()
    data class Error(val error: String) : Status()
}