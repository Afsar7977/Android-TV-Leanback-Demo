package com.afsar.kotlintv

import com.afsar.kotlintv.Modal.DataSource
import retrofit2.Call
import retrofit2.http.GET

interface Api {
    @GET("cecKccwamq?indent=2")
    fun getVideos(): Call<DataSource>
}