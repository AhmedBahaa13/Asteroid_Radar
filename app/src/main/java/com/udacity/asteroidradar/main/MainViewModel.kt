package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.MainImage
import com.udacity.asteroidradar.api.NasaApiProvider
import com.udacity.asteroidradar.api.getSeventhDay
import com.udacity.asteroidradar.api.getToday
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch


class MainViewModel(val app: Application) : AndroidViewModel(app) {

    private val db = AsteroidDatabase.getInstance(app)
    private val repository = AsteroidsRepository(db)

    private var _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    private val _displaySnackbarEvent = MutableLiveData<Boolean>()
    val displaySnackbarEvent: LiveData<Boolean>
        get() = _displaySnackbarEvent

    private val mainImageMutableLiveData = MutableLiveData<MainImage>()
    val mainImageLiveData: LiveData<MainImage>
        get() = mainImageMutableLiveData

    init {
        getData()
    }

    private fun getData() {
        // Get Main Image
        viewModelScope.launch {
            mainImageMutableLiveData.value = NasaApiProvider.retrofitService.getMainImage().body()
        }
        getTodayAsteroids()
    }

    fun getTodayAsteroids() {
        viewModelScope.launch {
            db.asteroidDao().getToDay(getToday()).collect{
                _asteroids.value = it
            }
        }
    }

    fun getWeekAsteroids() {
        viewModelScope.launch {
            db.asteroidDao().getWeek(getToday(), getSeventhDay()).collect{
                _asteroids.value = it
            }
        }
    }

    fun getSavedAsteroids() {
        viewModelScope.launch {
            db.asteroidDao().getAll().collect{
                _asteroids.value = it
            }
        }
    }


}