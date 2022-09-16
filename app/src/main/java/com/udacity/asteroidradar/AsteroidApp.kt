package com.udacity.asteroidradar

import android.app.Application
import androidx.work.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsteroidApp : Application() {
    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.Default).launch {
            createWorkManager()
        }
    }

    private fun createWorkManager() {
        val constraints =
            Constraints.Builder().setRequiresCharging(true).setRequiresBatteryNotLow(true)
                .setRequiredNetworkType(NetworkType.CONNECTED).build()

        val workRequest =
            PeriodicWorkRequestBuilder<DataWorker>(1, TimeUnit.DAYS).setConstraints(constraints)
                .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            DataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}