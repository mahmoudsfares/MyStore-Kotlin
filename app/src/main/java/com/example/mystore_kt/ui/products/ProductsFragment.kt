package com.example.mystore_kt.ui.products

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystore_kt.R
import com.example.mystore_kt.data.Product
import com.example.mystore_kt.databinding.FragmentProductsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : Fragment(R.layout.fragment_products) {

    private val viewModel by viewModels<ProductsViewModel>()

    private lateinit var productsAdapter: ProductsAdapter
    private val dummyProduct = listOf(Product(1, "iPhone 11", "u", 29.5))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentProductsBinding.bind(view)

        productsAdapter = ProductsAdapter(
            onProductClicked = {
                findNavController().navigate(
                    ProductsFragmentDirections.actionSplashFragmentToProductDetailsFragment(
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

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
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