package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import com.udacity.asteroidradar.Asteroid

@Database(entities = [Asteroid::class], version = 2, exportSchema = false)
abstract class AsteroidDatabase : RoomDatabase() {

    abstract fun asteroidDao():AsteroidDao

    companion object {
        @Volatile
        private var instance: AsteroidDatabase? = null
        fun getInstance(context: Context): AsteroidDatabase {
            return instance ?: Room.databaseBuilder(
                context.applicationContext,
                AsteroidDatabase::class.java,
                "asteroid_data_base"
            ).build()
        }
    }
}