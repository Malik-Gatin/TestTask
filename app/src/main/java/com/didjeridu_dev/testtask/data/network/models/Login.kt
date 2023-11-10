package com.didjeridu_dev.testtask.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class Login(
    val phone: String,
    val password: String
)