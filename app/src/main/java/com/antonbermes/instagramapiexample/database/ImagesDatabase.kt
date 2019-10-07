package com.antonbermes.instagramapiexample.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ImageDatabase::class, ImageDetailsDatabase::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class ImagesDatabase : RoomDatabase() {

    abstract val imagesDao: ImagesDao

    companion object {
        @Volatile
        private var INSTANCE: ImagesDatabase? = null

        fun getInstance(context: Context): ImagesDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ImagesDatabase::class.java,
                        "myDB"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}