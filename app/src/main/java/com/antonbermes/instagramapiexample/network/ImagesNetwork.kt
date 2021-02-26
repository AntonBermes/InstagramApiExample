package com.antonbermes.instagramapiexample.network

import com.antonbermes.instagramapiexample.database.ImageDatabase
import com.squareup.moshi.Json

data class ImagesNetwork(
    val paging: Paging?,
    val data: List<Data>
) {
    data class Paging(
        val cursors: Cursor
    ) {
        data class Cursor(
            val before: String,
            val after: String
        )
    }

    data class Data(
        var id: String,
        @Json(name = "media_url") val image: String,
        @Json(name = "media_type") val type: String
    )
}

fun ImagesNetwork.asDatabaseData(): List<ImageDatabase> {
    return data.map {
        ImageDatabase(
            imageId = it.id,
            url = it.image,
            type = it.type
        )
    }
}
