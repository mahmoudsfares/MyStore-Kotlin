package com.example.mystore_kt.ui.cart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystore_kt.R
import com.example.mystore_kt.databinding.FragmentCartBinding
import com.example.mystore_kt.networking.Resource
import com.example.mystore_kt.ui.ActivityViewModel
import com.example.mystore_kt.ui.cart.fragments.CartAdapter
import com.example.mystore_kt.ui.product_details.ProductDetailsFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart) {

    private val viewModel: CartViewModel by viewModels()
    private val activityViewModel: ActivityViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCartBinding.bind(view)

        binding.toolbar.apply {
            toolbarProfile.setOnClickListener {
                if (activityViewModel.userId == null) {
                    findNavController().navigate(
                        CartFragmentDirections.actionCartFragmentToLoggedOutFragment()
                    )
                } else {
                    //TODO: profile
                }
            }
            toolbarBack.setOnClickListener {
                activity?.onBackPressedDispatcher!!.onBackPressed()
            }
            toolbarTitle.text = "Cart"
        }



        activityViewModel.syncCart(null)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            activityViewModel.syncCartStatus.collect {
                if (it is Resource.Loading) {
                    binding.cartItemsLayout.visibility = View.INVISIBLE
                    binding.networkUi.root.visibility = View.VISIBLE
                    binding.networkUi.apply {
                        progressBar.visibility = View.VISIBLE
                        error.visibility = View.INVISIBLE
                        retryBtn.visibility = View.INVISIBLE
                    }
                } else if (it is Resource.Error) {
                    binding.cartItemsLayout.visibility = View.INVISIBLE
                    binding.networkUi.root.visibility = View.VISIBLE
                    binding.networkUi.apply {
                        progressBar.visibility = View.INVISIBLE
                        error.visibility = View.VISIBLE
                        error.text = it.error
                        retryBtn.visibility = View.VISIBLE
                    }
                } else {
                    binding.cartItemsLayout.visibility = View.VISIBLE
                    binding.networkUi.root.visibility = View.GONE
                }
            }
        }

        val adapter = CartAdapter(
            deleteItem = { cartItem -> activityViewModel.removeFromCart(cartItem) },
            changeQuantity = { cartItem -> activityViewModel.changeQuantityInCart(cartItem) }
        )

        binding.cartRv.adapter = adapter
        val layoutManager = LinearLayoutManager(context)
        val dividerItemDecoration =
            DividerItemDecoration(binding.cartRv.context, layoutManager.orientation)
        binding.cartRv.addItemDecoration(dividerItemDecoration)
        binding.cartRv.layoutManager = layoutManager

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.cartItems.collect {
                if (it == null || it.isEmpty()) {
                    binding.cartItemsLayout.visibility = View.INVISIBLE
                    binding.networkUi.root.visibility = View.VISIBLE
                    binding.networkUi.apply {
                        progressBar.visibility = View.INVISIBLE
                        error.visibility = View.VISIBLE
                        error.text = "No items in cart yet"
                        retryBtn.visibility = View.INVISIBLE
                    }
                } else {
                    adapter.submitList(it)
                    binding.cartItemsLayout.visibility = View.VISIBLE
                    binding.networkUi.root.visibility = View.GONE
                }
            }
        }

        binding.checkoutBtn.setOnClickListener {
//            if (activityViewModel.user == null) {
//                //TODO: bottom modal sheet for login / signup
//            } else {
//                //TODO: proceed to checkout
//            }
        }
    }
}