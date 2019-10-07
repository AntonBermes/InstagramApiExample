package com.antonbermes.instagramapiexample.network

import com.antonbermes.instagramapiexample.database.ImageDetailsDatabase
import com.squareup.moshi.Json

data class ImageDetailsNetwork(
    val data: Data
) {
    data class Data(
        var id: String?,
        val caption: Caption?,
        val images: ImagesMy
    ) {
        data class Caption(
            val text: String?
        )

        data class ImagesMy(
            @Json(name = "standard_resolution") val standardResolution: StandardResolution
        ) {
            data class StandardResolution(
                val url: String?
            )
        }
    }
}

fun ImageDetailsNetwork.asDatabaseImage(): ImageDetailsDatabase {
    return ImageDetailsDatabase(
        imageId = data.id ?: "",
        url = data.images.standardResolution.url ?: "",
        description = data.caption?.text ?: ""
    )
}