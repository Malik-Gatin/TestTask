package com.didjeridu_dev.testtask.domain.models

data class LocalPostData(
    val date: Long,
    val posts: List<PostDomain>
)
