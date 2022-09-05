package com.example.mystore_kt.ui.products.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mystore_kt.R
import com.example.mystore_kt.data.pojo.Product
import com.example.mystore_kt.databinding.ProductListItemBinding
import com.squareup.picasso.Picasso


/**
 * displayed data is provided in type PagingData<T>, T being the type of the object that populates one list item, not a list of that type
 * data is provided to its final function submitData(lifecycle, PagingData<T>)
 */
class PaginatedProductsAdapter(private val onProductClicked: (Product) -> Unit) :
    PagingDataAdapter<Product, PaginatedProductsAdapter.ProductsViewHolder>(PRODUCTS_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val binding = ProductListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductsViewHolder(binding, onProductClicked)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val currentItem = getItem(position)
        currentItem?.let { holder.bind(it) }
    }

    inner class ProductsViewHolder(
        private val binding: ProductListItemBinding,
        private val onProductClicked: (Product) -> Unit,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                // to avoid a crash if u click on an item while it is animating off the screen
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = getItem(bindingAdapterPosition)
                    if (clickedItem != null) {
                        onProductClicked(clickedItem)
                    }
                }
            }
        }

        fun bind(product: Product) {
            Picasso.get()
                .load(product.mainImageUrl)
                .fit()
                .placeholder(R.drawable.img_not_available)
                .into(binding.image)
            binding.name.text = product.name
            binding.price.text = "${product.price}"
        }
    }

    companion object {
        private val PRODUCTS_COMPARATOR = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem == newItem
        }
    }
}
