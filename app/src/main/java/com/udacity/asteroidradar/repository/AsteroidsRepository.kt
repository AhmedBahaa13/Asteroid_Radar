package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.DataWorker
import com.udacity.asteroidradar.api.NasaApiProvider
import com.udacity.asteroidradar.api.getSeventhDay
import com.udacity.asteroidradar.api.getToday
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AsteroidsRepository(
    private val database: AsteroidDatabase
) {
    companion object{
        val isFinished = MutableLiveData<Boolean>(false)
    }

    suspend fun getAsteroids() {
        withContext(Dispatchers.IO) {
            val response =
                NasaApiProvider.retrofitService.getAsteroid(getToday(), getSeventhDay())
            if (response.isSuccessful) {
                val asteroids = parseAsteroidsJsonResult(JSONObject(response.body()!!))
                Log.d("DataWorker", "asteroids Size: ${asteroids.size}")
                database.asteroidDao().insert(*asteroids.toTypedArray())
            }
            withContext(Dispatchers.Main){
                isFinished.value = true
            }
        }
    }
}