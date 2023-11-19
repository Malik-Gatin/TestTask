package com.didjeridu_dev.testtask.data.network.repository

import com.didjeridu_dev.testtask.data.local.SharedPreferencesManager
import com.didjeridu_dev.testtask.data.network.api.AuthenticationApiService
import com.didjeridu_dev.testtask.domain.models.Login
import com.didjeridu_dev.testtask.domain.models.AuthenticationDomain
import com.didjeridu_dev.testtask.domain.repository.AuthenticationRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.FormBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val  authenticationApiService: AuthenticationApiService,
    private val sharedPreferencesManager: SharedPreferencesManager
): AuthenticationRepository {
    override suspend fun auth(loginData: Login): Response<AuthenticationDomain> {
        val requestBody = createFormBody(loginData)
        val responseAuth = authenticationApiService.auth(requestBody)

        val responseAuthDomain = responseAuth.body()?.let { auth ->
            val authDomain: AuthenticationDomain = auth.castToDomain()
            Response.success(responseAuth.code(), authDomain)
        } ?: Response.error(responseAuth.code(), responseAuth.errorBody()!!)

        return responseAuthDomain
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
    private fun createFormBody(loginData: Login): RequestBody {
        return FormBody.Builder()
            .add("phone", loginData.phone)
            .add("password", loginData.password)
            .build()
    }
}