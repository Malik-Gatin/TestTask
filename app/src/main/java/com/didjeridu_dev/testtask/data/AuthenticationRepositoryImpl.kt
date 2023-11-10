package com.didjeridu_dev.testtask.data

import com.didjeridu_dev.testtask.data.network.models.Authentication
import com.didjeridu_dev.testtask.data.network.AuthenticationApiService
import com.didjeridu_dev.testtask.domain.repository.AuthenticationRepository
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
   private val  authenticationApiService: AuthenticationApiService
): AuthenticationRepository {
    override suspend fun auth(loginData: RequestBody): Response<Authentication> {
        return authenticationApiService.auth(loginData)
    }
}