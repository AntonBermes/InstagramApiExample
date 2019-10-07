package com.antonbermes.instagramapiexample.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class ImageDatabase(
    @PrimaryKey
    @ColumnInfo(name = "imageId")
    var imageId: String,
    @ColumnInfo(name = "url")
    var url: String,
    @ColumnInfo(name = "type")
    var type: String
)