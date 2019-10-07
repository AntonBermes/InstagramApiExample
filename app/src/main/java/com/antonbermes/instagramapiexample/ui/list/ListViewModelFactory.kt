package com.antonbermes.instagramapiexample.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.antonbermes.instagramapiexample.database.ImagesDao

class ListViewModelFactory(
    private val dataSource: ImagesDao,
    private val token: String
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            return ListViewModel(dataSource, token) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}