package com.antonbermes.instagramapiexample.repository

import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.antonbermes.instagramapiexample.database.ImagesDao
import com.antonbermes.instagramapiexample.domain.Image
import kotlinx.coroutines.CoroutineScope

class ListRepository(token: String, db: ImagesDao, scope: CoroutineScope): RepositoryInterface<PagedList<Image>> {

    companion object {
        private const val DATABASE_PAGE_SIZE = 20
    }

    private val callback = BoundaryCallback(token, db, scope)
    override val data = LivePagedListBuilder(db.getResult(), DATABASE_PAGE_SIZE)
        .setBoundaryCallback(callback)
        .build()
    override val status = callback.status

    fun update() {
        callback.update()
    }

    fun retry() {
        callback.retry()
    }
}