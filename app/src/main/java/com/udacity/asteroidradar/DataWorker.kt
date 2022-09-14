package com.udacity.asteroidradar

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.api.NasaApiProvider
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import org.json.JSONObject
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.*

class DataWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    override fun doWork(): Result {
        val dao = AsteroidDatabase.getInstance(applicationContext).asteroidDao()
        try {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
            val currentTime = dateFormat.format(calendar.time)
            calendar.add(Calendar.WEEK_OF_YEAR, 1)
            val dateAfterSevenDays = dateFormat.format(calendar.time)
            val response =
                NasaApiProvider.retrofitService.getAsteroid(currentTime, dateAfterSevenDays)
            return if (response.isSuccessful) {
                val asteroids = parseAsteroidsJsonResult(JSONObject(response.body()!!))
                Log.d("MainViewModel", "asteroids Size: ${asteroids.size}")
                asteroids.forEach { dao.insert(it) }
                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: SocketTimeoutException) {
            Log.d("MainViewModel", "doWork: ${e.message}")
            return Result.retry()
        }
    }

}