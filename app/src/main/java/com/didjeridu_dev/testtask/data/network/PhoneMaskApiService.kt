package com.didjeridu_dev.testtask.data.network

import com.didjeridu_dev.testtask.data.network.models.PhoneMask
import retrofit2.http.GET

interface PhoneMaskApiService {
    @GET("phone_masks")
    suspend fun getPhoneMask(): PhoneMask
}