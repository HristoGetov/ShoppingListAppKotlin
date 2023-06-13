package com.example.shoppinglistaappkotlin

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppinglistaappkotlin.View.ProductRecyclerViewAdapter
import com.example.shoppinglistaappkotlin.ViewModel.ProductViewModel
import com.example.shoppinglistaappkotlin.ViewModel.ProductViewModelFactory
import com.example.shoppinglistaappkotlin.databinding.ActivityMainBinding
import com.example.shoppinglistaappkotlin.room.Product
import com.example.shoppinglistaappkotlin.room.ProductDatabase
import com.example.shoppinglistaappkotlin.room.ProductRepository

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var  productViewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val dao = ProductDatabase.getInstance(application).productDAO
        val repository = ProductRepository(dao)
        val factory = ProductViewModelFactory(repository)
        productViewModel = ViewModelProvider(this,factory).get(ProductViewModel::class.java)

        binding.productViewModel = productViewModel

        binding.lifecycleOwner = this

        initRecyclerView()

    }

    private fun initRecyclerView() {
        binding.allProducts.layoutManager = LinearLayoutManager(this)
        displayUserList()
    }

    private fun displayUserList() {
        productViewModel.products.observe(this, Observer { 
            binding.allProducts.adapter = ProductRecyclerViewAdapter(it,{selectedItem: Product -> listItemClicked(selectedItem)})
        })
    }

    private fun listItemClicked(selectedItem: Product) {
        Toast.makeText(this,"Selected product is ${selectedItem.productName}",Toast.LENGTH_LONG).show()
        productViewModel.initUpdateAndDelete(selectedItem)

    }

}