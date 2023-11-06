package com.didjeridu_dev.testtask.data.network

import retrofit2.http.GET

interface PhoneMaskApiService {
    @GET("phone_masks")
    suspend fun getPhoneMask(): PhoneMask
}