package com.antonbermes.instagramapiexample.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "images_details",
    foreignKeys = [ForeignKey(
        entity = ImageDatabase::class,
        parentColumns = arrayOf("imageId"),
        childColumns = arrayOf("imageId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class ImageDetailsDatabase(
    @PrimaryKey
    @ColumnInfo(name = "imageId")
    var imageId: String,
    @ColumnInfo(name = "url")
    var url: String,
    @ColumnInfo(name = "description")
    var description: String
)