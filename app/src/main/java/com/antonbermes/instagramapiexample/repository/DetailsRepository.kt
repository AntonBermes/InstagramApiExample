package com.antonbermes.instagramapiexample.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.antonbermes.instagramapiexample.database.ImagesDao
import com.antonbermes.instagramapiexample.domain.ImageDetails
import com.antonbermes.instagramapiexample.network.InstaApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DetailsRepository(
    private val token: String,
    private val id: String,
    private val database: ImagesDao
): RepositoryInterface<ImageDetails> {

    override val data = database.getImageById(id)

    private val _status = MutableLiveData<Status>()
    override val status: LiveData<Status>
        get() = _status

    suspend fun refresh() {
        withContext(Dispatchers.IO) {
            try {
                _status.postValue(Status.StartLoading)
                val image = InstaApi.RETROFIT_SERVICE.getImageAsync(id, token).await()
                database.insert(image.asDatabaseImage())
                _status.postValue(Status.StopLoading)
            } catch (e: Exception) {
                _status.postValue(Status.Error(e.toString()))
            }
        }
    }
}