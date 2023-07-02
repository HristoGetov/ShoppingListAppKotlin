package com.example.shoppinglistaappkotlin.presentation.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglistaappkotlin.data.room.ProductRepository
import java.lang.IllegalArgumentException

class ProductViewModelFactory(private val repository: ProductRepository, private val context: Context): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)){
            return ProductViewModel(repository,context) as T
        }
        throw IllegalArgumentException("Unknown View Model class!")
    }
}