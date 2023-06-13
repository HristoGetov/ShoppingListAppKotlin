package com.example.shoppinglistaappkotlin.View

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.shoppinglistaappkotlin.R
import com.example.shoppinglistaappkotlin.databinding.CardItemBinding
import com.example.shoppinglistaappkotlin.room.Product

class ProductRecyclerViewAdapter(private val productList: List<Product>, private val clickListener: (Product) -> Unit): RecyclerView.Adapter<ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : CardItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.card_item,parent,false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productList[position],clickListener)
    }
}
class ProductViewHolder(val binding: CardItemBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(product: Product,clickListener: (Product) -> Unit){
        binding.productNameText.text = product.productName
        binding.quantityText.text = product.quantity
        binding.valueText.text = product.type

        binding.listItemLayout.setOnClickListener{
            clickListener(product)
        }

    }
}