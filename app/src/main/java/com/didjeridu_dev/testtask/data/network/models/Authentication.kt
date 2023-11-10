package com.didjeridu_dev.testtask.data.network.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Authentication(
    @SerializedName("success")
    val isSuccess:Boolean = false
)