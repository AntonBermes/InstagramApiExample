package com.antonbermes.instagramapiexample.domain

data class Image(
    val imageId: String,
    val url: String,
    val type: Type
)

enum class Type {IMAGE, CAROUSEL, VIDEO, ERROR}