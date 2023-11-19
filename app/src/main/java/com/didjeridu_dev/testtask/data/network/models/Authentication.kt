package com.didjeridu_dev.testtask.data.network.models

import com.didjeridu_dev.testtask.domain.models.AuthenticationDomain
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Authentication(
    @SerializedName("success")
    val isSuccess:Boolean = false
){
    fun castToDomain() = AuthenticationDomain(
        isSuccess = isSuccess
    )
}