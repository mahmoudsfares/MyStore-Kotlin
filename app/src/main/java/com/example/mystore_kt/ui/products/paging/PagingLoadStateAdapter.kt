package com.example.mystore_kt.ui.products.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mystore_kt.databinding.PagingErrorBinding
import java.net.UnknownHostException

/**
 * a reusable class that is responsible for inflating header and footer layout
 */
class PagingLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<PagingLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = PagingErrorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class LoadStateViewHolder(private val binding: PagingErrorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.buttonRetry.setOnClickListener {
                retry()
            }
        }

        fun bind(loadState: LoadState) {
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.buttonRetry.isVisible = loadState is LoadState.Error && loadState !is LoadState.Loading
            binding.textViewError.isVisible = loadState is LoadState.Error && loadState !is LoadState.Loading
            if (loadState is LoadState.Error) {
                val error = loadState.error
                if (error is UnknownHostException){
                    binding.buttonRetry.isVisible = true
                    binding.textViewError.text = "No internet connection."
                }
                else {
                    binding.buttonRetry.isVisible = true
                    binding.textViewError.text = "Error occurred."
                }
            }
        }
    }
}