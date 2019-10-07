package com.antonbermes.instagramapiexample.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.antonbermes.instagramapiexample.database.ImagesDao
import com.antonbermes.instagramapiexample.domain.Image
import com.antonbermes.instagramapiexample.repository.ListRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class ListViewModel(
    database: ImagesDao,
    token: String
) : ViewModel() {

    private val _navigateFlag = MutableLiveData<Image>()
    val navigateFlag: LiveData<Image>
        get() = _navigateFlag

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val repository = ListRepository(token, database, coroutineScope)

    val data = repository.data

    val status = repository.status

    val showRecyclerView: LiveData<Boolean>
        get() = Transformations.map(data) {
            !it.isEmpty()
        }

    fun listClicked(image: Image) {
        _navigateFlag.value = image
    }

    fun navigated() {
        _navigateFlag.value = null
    }

    fun update() {
        repository.update()
    }

    fun retry() {
        repository.retry()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}