package com.antonbermes.instagramapiexample.network

import com.antonbermes.instagramapiexample.BuildConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://api.instagram.com/v1/"

interface InstaClient {

    @POST("https://api.instagram.com/oauth/access_token")
    @FormUrlEncoded
    fun getAccessTokenAsync(
        @Field("code") code: String,
        @Field("client_id") clientId: String = BuildConfig.CLIENT_ID,
        @Field("client_secret") clientSecret: String = BuildConfig.CLIENT_SECRET,
        @Field("grant_type") grantType: String = "authorization_code",
        @Field("redirect_uri") redirectUri: String = BuildConfig.CALLBACK_URL
    ): Deferred<TokenNetwork>

    /**
     * We can get all the necessary information about the image in this request.
     * But, not all backends allow you to get all the information in one request,
     * so for practice we will make another request.
     */
    @GET("users/self/media/recent/")
    fun getListOfImagesAsync(
        @Query("access_token") token: String,
        @Query("count") count: Int,
        @Query("max_id") nextId: String?
    ): Deferred<ImagesNetwork>

    @GET("media/{id}/")
    fun getImageAsync(
        @Path("id") imageId: String,
        @Query("access_token") token: String
    ): Deferred<ImageDetailsNetwork>
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

object InstaApi {
    val RETROFIT_SERVICE: InstaClient by lazy { retrofit.create(InstaClient::class.java) }
}