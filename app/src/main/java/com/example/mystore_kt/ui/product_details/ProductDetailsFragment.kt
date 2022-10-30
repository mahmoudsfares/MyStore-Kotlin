package com.example.mystore_kt.ui.product_details

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mystore_kt.R
import com.example.mystore_kt.data.pojo.CartItem
import com.example.mystore_kt.data.pojo.DetailedProduct
import com.example.mystore_kt.data.pojo.WishlistItem
import com.example.mystore_kt.databinding.FragmentProductDetailsBinding
import com.example.mystore_kt.networking.Resource
import com.example.mystore_kt.ui.ActivityViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailsFragment : Fragment(R.layout.fragment_product_details) {

    private val viewModel: ProductDetailsViewModel by viewModels()
    private val activityViewModel: ActivityViewModel by activityViewModels()
    private val args: ProductDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentProductDetailsBinding.bind(view)

        //------------------- TEST --------------------//
        val product = DetailedProduct(1, "iPhone 11", "really good phone", "u", listOf("u"), 29.5)
        binding.toolbar.apply {
            toolbarTitle.text = product.name
            toolbarBack.setOnClickListener { activity?.onBackPressed() }
            cartAction.btnOpenCart.setOnClickListener {
                findNavController().navigate(ProductDetailsFragmentDirections.actionProductDetailsFragmentToCartFragment())
            }
            activityViewModel.cartItemsCount.observe(viewLifecycleOwner) {
                if (it == null || it == 0) {
                    cartAction.countBadge.visibility = View.INVISIBLE
                } else {
                    cartAction.countBadge.visibility = View.VISIBLE
                    cartAction.countBadge.text = "$it"
                }
            }
        }
        viewModel.test(product)
        binding.apply {
            networkUi.apply {
                progressBar.isVisible = false
                error.isVisible = false
                retryBtn.isVisible = false
            }
            cartPb.visibility = View.INVISIBLE
            wishlistPb.visibility = View.INVISIBLE
            viewModel.checkItemInCart(args.productId)
            viewModel.checkItemInFavourites(args.productId)
            image.isVisible = true
            data.isVisible = true
            actions.isVisible = true
            Picasso.get().load(product.mainImageUrl)
                .placeholder(R.drawable.img_not_available)
                .into(image)
            price.text = "Price: ${product.price}"
            description.text = product.description
        }
        //----------------------------------------------//


//        viewModel.getProductDetails(args.productId)
//
//        lifecycleScope.launchWhenStarted {
//            viewModel.productDetails.collect {
//                binding.apply {
//                    when (it) {
//                        is Resource.Loading -> {
//                            networkUi.apply {
//                                progressBar.isVisible = true
//                                error.isVisible = false
//                                retryBtn.isVisible = false
//                            }
//                            image.isVisible = false
//                            data.isVisible = false
//                            actions.isVisible = false
//                        }
//                        is Resource.Success -> {
//                            networkUi.apply {
//                                progressBar.isVisible = false
//                                error.isVisible = false
//                                retryBtn.isVisible = false
//                            }
//                            viewModel.startIsItemInCartFlow(args.productId)
//                            viewModel.startIsItemInFavouritesFlow(args.productId)
//                            image.isVisible = true
//                            data.isVisible = true
//                            actions.isVisible = true
//                            Picasso.get().load(it.data!!.mainImageUrl)
//                                .placeholder(R.drawable.img_not_available)
//                                .into(image)
//                            price.text = "Price: ${it.data.price}"
//                            description.text = it.data.description
//                        }
//                        is Resource.Error -> {
//                            networkUi.apply {
//                                progressBar.isVisible = false
//                                error.isVisible = true
//                                retryBtn.isVisible = true
//                            }
//                            image.isVisible = false
//                            data.isVisible = false
//                            actions.isVisible = false
//                        }
//                    }
//                }
//            }
//        }


        //--------------- CART ---------------//

        // bind item state in databse to the add to cart button
        lifecycleScope.launchWhenStarted {
            viewModel.isItemInCart.collect {
                val isInCart = it ?: return@collect
                if (isInCart) {
                    binding.cartBtn.setBackgroundColor(Color.parseColor("#00FF00"))
                    binding.cartBtn.text = "Added to cart"
                } else {
                    binding.cartBtn.setBackgroundColor(Color.parseColor("#000000"))
                    binding.cartBtn.text = "Add to cart"
                }
            }
        }

        // show the indicator that the sync in progress when it is to avoid multiple calls
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            activityViewModel.syncCartStatus.collect {
                val result = it ?: return@collect
                if (result is Resource.Loading) {
                    binding.cartPb.isVisible = true
                    binding.cartBtn.isVisible = false
                }
                if (result is Resource.Error) {
                    binding.cartPb.isVisible = false
                    binding.cartBtn.isVisible = true
                    Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                }
                if (result is Resource.Success) {
                    binding.cartPb.isVisible = false
                    binding.cartBtn.isVisible = true
                }
            }
        }

        // add to or remove from cart
        binding.cartBtn.setOnClickListener {
            val item = viewModel.productDetails.value.data!!
            val cartItem = CartItem(
                item.id,
                item.name,
                item.mainImageUrl
            )
            if (viewModel.isItemInCart.value!!) {
                activityViewModel.removeFromCart(cartItem)
            } else {
                activityViewModel.addToCart(cartItem)
            }
        }


        //--------------- WISHLIST ---------------//

        // bind item state in database to the add to wishlist icon
        lifecycleScope.launchWhenStarted {
            viewModel.isItemInWishlist.collect {
                val result = it ?: return@collect
                if (result) {
                    binding.wishlistIcon.setImageResource(R.drawable.ic_in_wishlist)
                } else {
                    binding.wishlistIcon.setImageResource(R.drawable.ic_out_wishlist)
                }
            }
        }

        // show the indicator that the sync in progress when it is to avoid multiple calls
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            activityViewModel.syncWishlistStatus.collect {
                val result = it ?: return@collect
                if (result is Resource.Loading) {
                    binding.wishlistPb.isVisible = true
                    binding.wishlistIcon.isVisible = false
                }
                if (result is Resource.Error) {
                    binding.wishlistPb.isVisible = false
                    binding.wishlistIcon.isVisible = true
                    Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                }
                if (result is Resource.Success) {
                    binding.wishlistPb.isVisible = false
                    binding.wishlistIcon.isVisible = true
                }
            }
        }

        // add to or remove from wishlist
        binding.wishlistIcon.setOnClickListener {
            val item = viewModel.productDetails.value.data!!
            val wishlistItem = WishlistItem(
                item.id,
                item.name,
                item.mainImageUrl
            )
            if (viewModel.isItemInWishlist.value!!) {
                activityViewModel.removeFromWishlist(wishlistItem)
            } else {
                activityViewModel.addToWishlist(wishlistItem)
            }
        }
    }
}