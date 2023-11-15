package com.didjeridu_dev.testtask.domain.models

import com.didjeridu_dev.testtask.data.network.models.Post

data class LocalPostData(
    val date: Long,
    val posts: List<Post>
)
