package com.example.shoppinglistaappkotlin.presentation.view

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglistaappkotlin.R
import com.example.shoppinglistaappkotlin.databinding.CardItemBinding
import com.example.shoppinglistaappkotlin.data.room.Product
import com.example.shoppinglistaappkotlin.data.room.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductRecyclerViewAdapter(private val productList: List<Product>, private val productRepository: ProductRepository, private val clickListener: (Product) -> Unit): RecyclerView.Adapter<ProductViewHolder>() {

    private lateinit var builder:AlertDialog.Builder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        builder = AlertDialog.Builder(parent.context)
        val binding : CardItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.card_item,parent,false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productList[position],clickListener)
        holder.binding.deleteButton.setOnClickListener{
            builder.setTitle(Html.fromHtml("<b>Изтриване на продукт!</b>"))
            builder.setMessage("Сигурни ли сте, че искате да изтриете продукта?")
            builder.setPositiveButton("Не"){ dialog, which ->

            }
            builder.setNegativeButton("Да"){dialog,which ->
                CoroutineScope(Dispatchers.IO).launch {
                    productRepository.delete(productList[position])
                }
            }
            val alertDialog = builder.create()
            alertDialog.show()
            alertDialog.window?.setBackgroundDrawableResource(android.R.color.holo_orange_light)
        }
    }
}


class ProductViewHolder(val binding: CardItemBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(product: Product, clickListener: (Product) -> Unit){
        binding.productNameText.text = product.productName
        binding.quantityText.text = product.quantity
        binding.valueText.text = product.type
        binding.listItemLayout.setOnClickListener{
            clickListener(product)
        }

    }
}