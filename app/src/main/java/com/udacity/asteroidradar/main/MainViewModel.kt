package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.MainImage
import com.udacity.asteroidradar.api.NasaApiProvider
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
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
        getTodayAsteroids()
    }

   private fun getData(getDay:Boolean) {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        currentTime = dateFormat.format(calendar.time)
        calendar.add(Calendar.WEEK_OF_YEAR,1)
        dateAfterSevenDays = dateFormat.format(calendar.time)
        viewModelScope.launch(Dispatchers.IO) {
            val response = when(getDay){
                true ->  NasaApiProvider.retrofitService.getAsteroid(currentTime, currentTime)
                false -> NasaApiProvider.retrofitService.getAsteroid(currentTime, dateAfterSevenDays)
            }
            val asteroids = parseAsteroidsJsonResult(JSONObject(response))
            Log.d("MainViewModel", "asteroids Size: ${asteroids.size}")
            asteroids.forEach {
                dao.insert(it)
            }
            withContext(Dispatchers.Main){
                mainImageMutableLiveData.value = NasaApiProvider.retrofitService.getMainImage()
            }
        }
    }
    fun getTodayAsteroids(){
        getData(true)
        viewModelScope.launch(Dispatchers.IO) {
            val data =  dao.getToDay(currentTime)
            withContext(Dispatchers.Main){
                asteroidList.value = data
                showProgressBar.value = false
            }
        }
    }

    fun getWeekAsteroids(){
        getData(false)
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