package com.example.mystore_kt.ui.home

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystore_kt.R
import com.example.mystore_kt.data.Product
import com.example.mystore_kt.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var productsAdapter: ProductsAdapter
    private val dummyProduct = listOf(Product(1, "iPhone 11", "u", 29.5))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHomeBinding.bind(view)

        productsAdapter = ProductsAdapter(
            onProductClicked = {}
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