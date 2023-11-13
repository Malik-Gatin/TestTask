package com.didjeridu_dev.testtask.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: String,
    val title:String,
    val text: String,
    val image: String,
    val sort: Int,
    val date: String
)
