package com.didjeridu_dev.testtask.domain.repository

import com.didjeridu_dev.testtask.domain.models.Login
import com.didjeridu_dev.testtask.domain.models.AuthenticationDomain
import retrofit2.Response

interface AuthenticationRepository {
    suspend fun auth(loginData: Login): Response<AuthenticationDomain>
    suspend fun savePhoneAndPassword(phoneMask:String, loginData: Login)
    suspend fun getLoginDataFromLocalFile(phoneMask:String) : Login?
}