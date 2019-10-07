package com.antonbermes.instagramapiexample.database

import androidx.room.TypeConverter
import com.antonbermes.instagramapiexample.domain.Type

class Converter {
    @TypeConverter
    fun stringToType(s: String): Type {
        return when (s) {
            "image" -> Type.IMAGE
            "carousel" -> Type.CAROUSEL
            "video" -> Type.VIDEO
            else -> Type.ERROR
        }
    }
}