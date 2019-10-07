package com.antonbermes.instagramapiexample.database

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.antonbermes.instagramapiexample.domain.Image
import com.antonbermes.instagramapiexample.domain.ImageDetails

@Dao
interface ImagesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(images: List<ImageDatabase>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(image: ImageDetailsDatabase)

    @Query("SELECT * FROM images")
    fun getResult(): DataSource.Factory<Int, Image>

    @Query("SELECT * FROM images_details WHERE imageId = :id")
    fun getImageById(id: String): LiveData<ImageDetails>

    @Query("DELETE FROM images")
    fun clear()

    @Transaction
    fun update(repos: List<ImageDatabase>) {
        clear()
        insert(repos)
    }
}