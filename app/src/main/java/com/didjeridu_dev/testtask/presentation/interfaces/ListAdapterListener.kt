package com.didjeridu_dev.testtask.presentation.interfaces

import com.didjeridu_dev.testtask.data.network.models.Post

interface ListAdapterListener {
    fun onItemClickListener(post: Post)
}