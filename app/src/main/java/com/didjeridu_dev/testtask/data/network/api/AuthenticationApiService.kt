package com.didjeridu_dev.testtask.data.network.api

import com.didjeridu_dev.testtask.data.network.models.Authentication
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthenticationApiService {

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("auth")
    suspend fun auth(@Body loginData: RequestBody): Response<Authentication>
}