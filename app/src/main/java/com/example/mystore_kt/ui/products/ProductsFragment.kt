package com.example.mystore_kt.ui.products

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystore_kt.R
import com.example.mystore_kt.data.pojo.Product
import com.example.mystore_kt.databinding.FragmentProductsBinding
import com.example.mystore_kt.ui.ActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : Fragment(R.layout.fragment_products) {

    private val viewModel by viewModels<ProductsViewModel>()
    private val activityViewModel: ActivityViewModel by activityViewModels()

    private lateinit var productsAdapter: ProductsAdapter
    private val dummyProduct = listOf(Product(1, "iPhone 11", "u", 29.5))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentProductsBinding.bind(view)

        // Toolbar and Drawer

        binding.toolbar.apply {
            cartAction.btnOpenCart.setOnClickListener {
                findNavController().navigate(
                    ProductsFragmentDirections.actionProductsFragmentToCartFragment()
                )
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

        binding.toolbar.toolbarMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        if (activityViewModel.userId == null){
            binding.drawer.inflateHeaderView(R.layout.drawer_logged_out)
        } else {
            binding.drawer.inflateHeaderView(R.layout.drawer_logged_in)
        }

        //-----------------------------------------------------------------------------//


        productsAdapter = ProductsAdapter(
            onProductClicked = {
                findNavController().navigate(
                    ProductsFragmentDirections.actionProductsFragmentToProductDetailsFragment(
                        it.id,
                        it.name
                    )
                )
            }
        )
        productsAdapter.submitList(dummyProduct)

        binding.productsRv.apply {
            adapter = productsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.search.clearFocus()
                Toast.makeText(context, "Clicked..", Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }
}