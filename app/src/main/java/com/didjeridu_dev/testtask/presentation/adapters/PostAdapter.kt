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
import com.didjeridu_dev.testtask.databinding.ArticleItemBinding
import com.didjeridu_dev.testtask.domain.models.PostDomain
import com.didjeridu_dev.testtask.presentation.interfaces.ListAdapterListener

class PostAdapter(
    private val listAdapterListener: ListAdapterListener
) : ListAdapter<PostDomain, PostAdapter.PostViewHolder>(PostDiff()) {

    class PostViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val binding = ArticleItemBinding.bind(view)

        fun bind(post:PostDomain, listAdapterListener: ListAdapterListener) = with(binding){
            tvTitle.text = post.title
            tvDescription.text = post.text
            tvDate.text = post.date
            val imageUrl = "$BASE_URL${post.image}"
            imageArticle.load(imageUrl){
                placeholder(R.drawable.loading_img)
                error(R.drawable.ic_broken_image)
            }
            itemView.setOnClickListener{
                listAdapterListener.onItemClickListener(post)
            }
        }
    }

    class PostDiff: DiffUtil.ItemCallback<PostDomain>(){
        override fun areItemsTheSame(oldItem: PostDomain, newItem: PostDomain): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PostDomain, newItem: PostDomain): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.article_item, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position), listAdapterListener)
    }
}