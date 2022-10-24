package com.example.mystore_kt.ui.cart.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mystore_kt.R
import com.example.mystore_kt.data.pojo.CartItem
import com.example.mystore_kt.databinding.ItemCartBinding
import com.squareup.picasso.Picasso

class CartAdapter(
    private val deleteItem: (cartItem: CartItem) -> Unit,
    private val changeQuantity: (cartItem: CartItem) -> Unit
) : ListAdapter<CartItem, CartAdapter.CartAdapterViewHolder>(CartItemComparator()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapterViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartAdapterViewHolder(binding, deleteItem, changeQuantity)
    }

    override fun onBindViewHolder(holder: CartAdapterViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class CartAdapterViewHolder(
        private val binding: ItemCartBinding,
        private val deleteItem: (cartItem: CartItem) -> Unit,
        private val changeQuantity: (cartItem: CartItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                quantitySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long)    {
                    val cartItem = getItem(bindingAdapterPosition)
                    //copy object in order to have a new reference, so the stateflow
                    // monitoring the Query returning a FLow would trigger.
                    //This is because Stateflow will not trigger if the same values are set to it again,
                    //unlike when using livedata, where this was not the case.
                    val updatedCartItem = cartItem.copy(quantity = position + 1)
                    changeQuantity(updatedCartItem)
                }
                override fun onNothingSelected(parent: AdapterView<*>) {}
                }

                remove.setOnClickListener {
                    val cartItem = getItem(bindingAdapterPosition)
                    deleteItem(cartItem)
                }
            }
        }

        fun bind(cartItem: CartItem) {
            binding.apply {
                cartItemName.text = cartItem.name
                quantitySpinner.setSelection(cartItem.quantity - 1)
                try {
                    Picasso.get()
                        .load(cartItem.mainImageUrl)
                        .placeholder(R.drawable.img_not_available)
                        .into(binding.cartItemImage)
                } catch (e: Exception) {
                }
            }
        }
    }

    class CartItemComparator : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean =
        // Compares if the item is the same based on business logic to detect if an item has changed position in the list and thus display
            //the correct animation
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean =
            //Compares if the item details has changed, so it can only rebind it again
            oldItem == newItem //data class auto-implements the .equals()
    }
}
