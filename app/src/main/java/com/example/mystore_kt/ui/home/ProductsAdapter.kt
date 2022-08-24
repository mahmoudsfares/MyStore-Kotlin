package com.example.mystore_kt.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mystore_kt.R
import com.example.mystore_kt.data.Product
import com.example.mystore_kt.databinding.ProductListItemBinding
import com.squareup.picasso.Picasso

class ProductsAdapter(
    private val onProductClicked: (Product) -> Unit
) :
    ListAdapter<Product, ProductsAdapter.ProductsViewHolder>(CategoryComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val binding =
            ProductListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductsViewHolder(binding, onProductClicked = { position ->
            val category = getItem(position)
            if (category != null) {
                onProductClicked(category)
            }
        })
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val category = getItem(position)
        if (category != null) {
            holder.bind(category)
        }
    }

    inner class ProductsViewHolder(
        private val binding: ProductListItemBinding,
        private val onProductClicked: (Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) //To avoid a crash if u click on an item while it is animating off the screen
                {
                    onProductClicked(bindingAdapterPosition)
                }
            }
        }

        fun bind(product: Product) {
            binding.apply {
                Picasso.get().load(product.mainImageUrl)
                    .placeholder(R.drawable.img_not_available)
                    .into(image)
                name.text = product.name
                price.text = "${product.price}"
            }
        }
    }

    class CategoryComparator : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newProduct: Product): Boolean =
        //Compares if the item is the same based on business logic to detect if an item has changed position in the list and thus display
            //the correct animation
            oldItem.id == newProduct.id

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
            //Compares if the item details has changed, so it can only rebind it again
            oldItem == newItem //data class auto-implements the .equals()
    }
}