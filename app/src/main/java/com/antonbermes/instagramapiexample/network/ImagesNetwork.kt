package com.antonbermes.instagramapiexample.network

import com.antonbermes.instagramapiexample.database.ImageDatabase
import com.squareup.moshi.Json

data class ImagesNetwork(
    val pagination: NextMaxId,
    val data: List<Data>
) {
    data class NextMaxId(
        @Json(name = "next_max_id") var nextId: String? = null
    )

    data class Data(
        var id: String?,
        val images: ImagesMy,
        val type: String?
    ) {
        data class ImagesMy(
            @Json(name = "thumbnail") val thumbnail: Thumbnail
        ) {
            data class Thumbnail(
                val url: String?
            )
        }
    }
}

fun ImagesNetwork.asDatabaseData(): List<ImageDatabase> {
    return data.map {
        ImageDatabase(
            imageId = it.id ?: "",
            url = it.images.thumbnail.url ?: "",
            type = it.type ?: ""
        )
    }
}
