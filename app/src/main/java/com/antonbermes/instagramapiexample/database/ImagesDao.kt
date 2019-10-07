package com.antonbermes.instagramapiexample.database

import androidx.room.*
import androidx.paging.DataSource
import com.antonbermes.instagramapiexample.domain.Image

@Dao
interface ImagesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(imageDatabases: List<ImageDatabase>)

    @Query("SELECT * FROM images")
    fun getResult(): DataSource.Factory<Int, Image>

    @Query("DELETE FROM images")
    fun clear()

    @Transaction
    fun update(repos: List<ImageDatabase>) {
        clear()
        insert(repos)
    }
}