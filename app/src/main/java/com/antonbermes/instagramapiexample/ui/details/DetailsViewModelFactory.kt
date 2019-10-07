package com.antonbermes.instagramapiexample.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.antonbermes.instagramapiexample.database.ImagesDao

class DetailsViewModelFactory(
    private val dataSource: ImagesDao,
    private val token: String,
    private val imageId: String
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            return DetailsViewModel(dataSource, token, imageId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}