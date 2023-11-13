package com.didjeridu_dev.testtask.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.didjeridu_dev.testtask.databinding.FragmentHomeBinding
import com.didjeridu_dev.testtask.presentation.adapters.PostAdapter
import com.didjeridu_dev.testtask.presentation.viewmodels.HomeViewModel
import com.didjeridu_dev.testtask.presentation.viewmodels.PostsApiStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment: Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PostAdapter()
        binding.recyclerViewArticles.adapter = adapter

        binding.bRefresh.setOnClickListener{
            homeViewModel.getPosts()
        }

        homeViewModel.status.observe(viewLifecycleOwner){postsStatus->
            postsStatus?.let {
                when(it){
                    PostsApiStatus.DONE -> {
                        binding.bRefresh.isEnabled = true
                    }
                    PostsApiStatus.LOADING -> {
                        binding.bRefresh.isEnabled = false
                    }
                    PostsApiStatus.ERROR -> {
                        binding.bRefresh.isEnabled = true
                    }
                }
            }
        }

        homeViewModel.posts.observe(viewLifecycleOwner){posts->
            posts?.let{
                adapter.submitList(posts)
            }
        }
    }
}