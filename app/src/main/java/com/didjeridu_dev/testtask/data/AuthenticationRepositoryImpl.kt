package com.didjeridu_dev.testtask.data

import com.didjeridu_dev.testtask.data.local.SharedPreferencesManager
import com.didjeridu_dev.testtask.data.network.models.Authentication
import com.didjeridu_dev.testtask.data.network.AuthenticationApiService
import com.didjeridu_dev.testtask.data.network.models.Login
import com.didjeridu_dev.testtask.domain.repository.AuthenticationRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
   private val  authenticationApiService: AuthenticationApiService,
   private val sharedPreferencesManager: SharedPreferencesManager
): AuthenticationRepository {
    override suspend fun auth(loginData: RequestBody): Response<Authentication> {
        return authenticationApiService.auth(loginData)
    }
    override suspend fun savePhoneAndPassword(phoneMask:String, loginData: Login) {
        val jsonLogin = Gson().toJson(loginData)
        sharedPreferencesManager.saveData(phoneMask,jsonLogin)
    }

    override suspend fun getLoginDataFromLocalFile(phoneMask:String) : Login? {
        val jsonPosts = sharedPreferencesManager.getData(phoneMask)
        return if(jsonPosts != null){
            Gson().fromJson(jsonPosts, object : TypeToken<Login>() {}.type)
        }else {
            null
        }
    }
}