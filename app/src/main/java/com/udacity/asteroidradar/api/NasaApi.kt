package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.MainImage
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://api.nasa.gov/"


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()



interface NasaApi {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroid(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String = Constants.API_KEY
    ): Response<String>

    @GET("planetary/apod")
    suspend fun getMainImage(
        @Query("api_key") apiKey: String = Constants.API_KEY
    ): Response<MainImage>
}

object NasaApiProvider {
    private val retrofit:Retrofit
    private val logging = HttpLoggingInterceptor()
    init {
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder().readTimeout(10,TimeUnit.SECONDS)
        httpClient.addInterceptor(logging)
         retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
             .client(httpClient.build())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    val retrofitService: NasaApi by lazy { retrofit.create(NasaApi::class.java) }
}