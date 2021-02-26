package com.antonbermes.instagramapiexample.database

import androidx.room.TypeConverter
import com.antonbermes.instagramapiexample.domain.Type

class Converter {
    @TypeConverter
    fun stringToType(s: String): Type {
        return when (s) {
            "IMAGE" -> Type.IMAGE
            "CAROUSEL_ALBUM" -> Type.CAROUSEL
            "VIDEO" -> Type.VIDEO
            else -> Type.ERROR
        }
    }
}