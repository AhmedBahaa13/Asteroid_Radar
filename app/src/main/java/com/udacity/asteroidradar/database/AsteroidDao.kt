package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.Asteroid
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg asteroid: Asteroid)

    @Query("select * from asteroids Where closeApproachDate = :date")
    fun getToDay(date: String): Flow<List<Asteroid>>

    @Query("SELECT * FROM asteroids Where closeApproachDate BETWEEN  :startDate AND :endDate ORDER BY closeApproachDate ASC  ")
    fun getWeek(startDate: String,endDate: String): Flow<List<Asteroid>>

    @Query("SELECT * FROM asteroids ORDER BY closeApproachDate ASC ")
    fun getAll(): Flow<List<Asteroid>>
}