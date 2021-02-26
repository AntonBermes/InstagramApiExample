package com.antonbermes.instagramapiexample.network

import com.antonbermes.instagramapiexample.database.ImageDetailsDatabase
import com.squareup.moshi.Json

data class ImageDetailsNetwork(
    var id: String,
    @Json(name = "media_url") val image: String,
    val caption: String?
) {
    fun asDatabaseImage(): ImageDetailsDatabase {
        return ImageDetailsDatabase(
            imageId = id,
            url = image,
            description = caption ?: ""
        )
    }
}