package com.didjeridu_dev.testtask.domain.repository

import com.didjeridu_dev.testtask.data.network.models.Authentication
import com.didjeridu_dev.testtask.data.network.models.Login
import okhttp3.RequestBody
import retrofit2.Response

interface AuthenticationRepository {
    suspend fun auth(loginData: RequestBody): Response<Authentication>
    suspend fun savePhoneAndPassword(phoneMask:String, loginData: Login)
    suspend fun getLoginDataFromLocalFile(phoneMask:String) : Login?
}