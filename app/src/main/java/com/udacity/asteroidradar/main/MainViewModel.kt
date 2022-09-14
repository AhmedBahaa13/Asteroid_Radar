package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import androidx.work.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.DataWorker
import com.udacity.asteroidradar.MainImage
import com.udacity.asteroidradar.api.NasaApiProvider
import com.udacity.asteroidradar.database.AsteroidDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


class MainViewModel(val app:Application) : AndroidViewModel(app) {
    private var currentTime = ""
    private var dateAfterSevenDays = ""

    private val dao = AsteroidDatabase.getInstance(app).asteroidDao()

    val showProgressBar = MutableLiveData<Boolean>(true)

    private val asteroidList = MutableLiveData<List<Asteroid>?>()
    val asteroidListLiveData: LiveData<List<Asteroid>?>
        get() = asteroidList

    private val mainImageMutableLiveData = MutableLiveData<MainImage>()
    val mainImageLiveData: LiveData<MainImage>
        get() = mainImageMutableLiveData

    init {
        getData()
        getTodayAsteroids()
    }

   private fun getData() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        currentTime = dateFormat.format(calendar.time)
        calendar.add(Calendar.WEEK_OF_YEAR,1)
        dateAfterSevenDays = dateFormat.format(calendar.time)
       viewModelScope.launch{
           mainImageMutableLiveData.value = NasaApiProvider.retrofitService.getMainImage().body()
       }

       val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
       val onetimeJob: OneTimeWorkRequest = OneTimeWorkRequest.Builder(DataWorker::class.java)
           .setConstraints(constraints).build()

       WorkManager.getInstance().enqueue(onetimeJob)
    }
    fun getTodayAsteroids(){
        viewModelScope.launch(Dispatchers.IO) {
            val data =  dao.getToDay(currentTime)
            withContext(Dispatchers.Main){
                asteroidList.value = data
                showProgressBar.value = false
            }
        }
    }

    fun getWeekAsteroids(){
        viewModelScope.launch(Dispatchers.IO) {
            val data =  dao.getWeek(currentTime,dateAfterSevenDays)
            withContext(Dispatchers.Main){
                asteroidList.value = data
                showProgressBar.value = false
            }
        }
    }

    fun getSavedAsteroids() {
        viewModelScope.launch(Dispatchers.IO) {
        val data =  dao.getAll()
            withContext(Dispatchers.Main){
                asteroidList.value = data
                showProgressBar.value = false
            }
        }

    }




}