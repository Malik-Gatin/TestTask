package com.didjeridu_dev.testtask.data.network.models

import com.didjeridu_dev.testtask.domain.models.PostDomain
import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: String,
    val title:String,
    val text: String,
    val image: String,
    val sort: Int,
    val date: String
){
    fun castToDomain() = PostDomain(
        id = id,
        title = title,
        text =text,
        image = image,
        sort = sort,
        date = date
    )
}
