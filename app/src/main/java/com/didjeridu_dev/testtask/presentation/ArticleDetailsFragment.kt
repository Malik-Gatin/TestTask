package com.didjeridu_dev.testtask.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import com.didjeridu_dev.testtask.App
import com.didjeridu_dev.testtask.App.AppConstants.SELECTED_ITEM
import com.didjeridu_dev.testtask.R
import com.didjeridu_dev.testtask.databinding.FragmentArticleDetailsBinding
import com.didjeridu_dev.testtask.domain.models.PostDomain
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class ArticleDetailsFragment:Fragment() {

    private lateinit var binding: FragmentArticleDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectedItem:PostDomain? = arguments?.getParcelable(SELECTED_ITEM)
        selectedItem?.let {
            with(binding){
                val imageUrl = "${App.AppConstants.BASE_URL}${it.image}"
                imImage.load(imageUrl){
                    placeholder(R.drawable.loading_img)
                    error(R.drawable.ic_broken_image)
                }
                tvDate.text = it.date
                tvTitle.text = it.title
                tvDescription.text = it.text
            }
        } ?: {
            with(binding){
                imImage.setImageResource(R.drawable.ic_broken_image)
                tvTitle.text = getString(R.string.error_selected_item)
            }

        }

        binding.toolbarBBack.setOnClickListener{
            findNavController().navigateUp()
        }
        binding.toolbarTitle.text = selectedItem?.title
    }
}