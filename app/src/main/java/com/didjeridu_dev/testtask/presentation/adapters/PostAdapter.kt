package com.didjeridu_dev.testtask.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.didjeridu_dev.testtask.App.AppConstants.BASE_URL
import com.didjeridu_dev.testtask.R
import com.didjeridu_dev.testtask.data.network.models.Post
import com.didjeridu_dev.testtask.databinding.ArticleItemBinding

class PostAdapter : ListAdapter<Post, PostAdapter.PostViewHolder>(PostDiff()) {

    class PostViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val binding = ArticleItemBinding.bind(view)

        fun bind(post:Post) = with(binding){
            tvTitle.text = post.title
            tvDescription.text = post.text
            tvDate.text = post.date
            val imageUrl = "$BASE_URL${post.image}"
            imageArticle.load(imageUrl){
                placeholder(R.drawable.loading_img)
                error(R.drawable.ic_broken_image)
            }
        }
    }

    class PostDiff: DiffUtil.ItemCallback<Post>(){
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.article_item, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}