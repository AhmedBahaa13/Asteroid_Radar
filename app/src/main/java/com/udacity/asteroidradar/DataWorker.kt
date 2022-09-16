package com.udacity.asteroidradar

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import java.net.SocketTimeoutException

class DataWorker(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {
    companion object{
        const val WORK_NAME = "GetData"
    }

    override suspend fun doWork(): Result {
        val db = AsteroidDatabase.getInstance(applicationContext)
        val repo = AsteroidsRepository(db,)
        return try {
            repo.getAsteroids()

            Result.success()
        } catch (e: SocketTimeoutException) {
            Log.d("MainViewModel", "doWork: ${e.message}")
            Result.retry()
        }
    }

}