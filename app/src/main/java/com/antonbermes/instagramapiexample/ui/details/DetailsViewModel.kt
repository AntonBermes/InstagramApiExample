package com.antonbermes.instagramapiexample.ui.details

import androidx.lifecycle.ViewModel
import com.antonbermes.instagramapiexample.database.ImagesDao
import com.antonbermes.instagramapiexample.repository.DetailsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetailsViewModel(
    database: ImagesDao,
    token: String,
    imageId: String
) : ViewModel() {

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val repository = DetailsRepository(token, imageId, database)

    init {
        refresh()
    }

    val data = repository.data

    val status = repository.status

    fun refresh () {
        coroutineScope.launch {
            repository.refresh()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}