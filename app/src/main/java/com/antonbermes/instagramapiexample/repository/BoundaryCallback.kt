package com.antonbermes.instagramapiexample.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.antonbermes.instagramapiexample.database.ImagesDao
import com.antonbermes.instagramapiexample.domain.Image
import com.antonbermes.instagramapiexample.network.InstaApi
import com.antonbermes.instagramapiexample.network.ImagesNetwork
import com.antonbermes.instagramapiexample.network.asDatabaseData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BoundaryCallback(
    private val token: String,
    private val database: ImagesDao,
    private val scope: CoroutineScope
) : PagedList.BoundaryCallback<Image>() {

    companion object {
        private const val NETWORK_PAGE_SIZE = 5
    }

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    private var nextId: String? = ""

    private var isInitialized = false
    private var isLoading = false

    init {
        requestAndSaveData()
    }

    override fun onZeroItemsLoaded() {
        requestAndSaveData()
    }

    override fun onItemAtEndLoaded(itemAtEnd: Image) {
        requestAndSaveData()
    }

    private fun requestAndSaveData() {
        if (nextId == null || isLoading) return
        isLoading = true
        if (!isInitialized) _status.value = Status.StartLoading
        scope.launch {
            try {
                val images = getNetworkData()
                updateDB(images)
                nextId = images.paging?.cursors?.after
                _status.value = Status.StopLoading
                isInitialized = true
            } catch (e: Exception) {
                _status.value = Status.Error(e.toString())
            }
            isLoading = false
        }
    }

    private suspend fun getNetworkData(): ImagesNetwork {
        return InstaApi.RETROFIT_SERVICE.getListOfImagesAsync(token, NETWORK_PAGE_SIZE, nextId)
            .await()
    }

    private suspend fun updateDB(networkData: ImagesNetwork) {
        withContext(Dispatchers.IO) {
            if (isInitialized) database.insert(networkData.asDatabaseData())
            else database.update(networkData.asDatabaseData())
        }
    }

    fun update() {
        if (isLoading) return
        isInitialized = false
        nextId = ""
        requestAndSaveData()
    }

    fun retry() {
        requestAndSaveData()
    }
}