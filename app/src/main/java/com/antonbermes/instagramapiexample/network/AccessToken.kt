package com.antonbermes.instagramapiexample.network

import com.squareup.moshi.Json

data class AccessToken(
    @Json(name = "access_token") val accessToken: String
)