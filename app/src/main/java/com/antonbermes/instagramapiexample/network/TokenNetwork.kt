package com.antonbermes.instagramapiexample.network

import com.squareup.moshi.Json

data class TokenNetwork(
    @Json(name = "access_token") val accessToken: String
)