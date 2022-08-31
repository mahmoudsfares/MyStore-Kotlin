package com.example.mystore_kt.ui.product_details

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.mystore_kt.R
import com.example.mystore_kt.databinding.FragmentProductDetailsBinding
import com.example.mystore_kt.networking.Resource
import com.example.mystore_kt.ui.ActivityViewModel
import com.squareup.picasso.Picasso

class ProductDetailsFragment : Fragment(R.layout.fragment_product_details) {

    private val viewModel: ProductDetailsViewModel by viewModels()
    private val activityViewModel: ActivityViewModel by activityViewModels()
    private val args: ProductDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentProductDetailsBinding.bind(view)

        viewModel.getProductDetails(args.productId)

        lifecycleScope.launchWhenStarted {
            viewModel.productDetails.collect {
                binding.apply {
                    when (it){
                        is Resource.Loading -> {
                            networkUi.apply {
                                progressBar.isVisible = true
                                error.isVisible = false
                                retryBtn.isVisible = false
                            }
                            image.isVisible = false
                            data.isVisible = false
                            actions.isVisible = false
                        }
                        is Resource.Success -> {
                            networkUi.apply {
                                progressBar.isVisible = false
                                error.isVisible = false
                                retryBtn.isVisible = false
                            }
                            image.isVisible = true
                            data.isVisible = true
                            actions.isVisible = true
                            Picasso.get().load(it.data!!.mainImageUrl)
                                .placeholder(R.drawable.img_not_available)
                                .into(image)
                            price.text = "Price: ${it.data.price}"
                            description.text = it.data.description
                        }
                        is Resource.Error -> {
                            networkUi.apply {
                                progressBar.isVisible = false
                                error.isVisible = true
                                retryBtn.isVisible = true
                            }
                            image.isVisible = false
                            data.isVisible = false
                            actions.isVisible = false
                        }
                    }
                }
            }
        }
    }
}