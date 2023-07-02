package com.example.shoppinglistaappkotlin.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppinglistaappkotlin.R
import com.example.shoppinglistaappkotlin.presentation.view.ProductRecyclerViewAdapter
import com.example.shoppinglistaappkotlin.presentation.viewModel.ProductViewModel
import com.example.shoppinglistaappkotlin.presentation.viewModel.ProductViewModelFactory
import com.example.shoppinglistaappkotlin.databinding.ActivityMainBinding
import com.example.shoppinglistaappkotlin.data.room.Product
import com.example.shoppinglistaappkotlin.data.room.ProductDatabase
import com.example.shoppinglistaappkotlin.data.room.ProductRepository
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var  productViewModel: ProductViewModel
    private lateinit var repository: ProductRepository
    private lateinit var firebaseAuth: FirebaseAuth

    companion object{
        private lateinit var instance: MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this
        firebaseAuth = FirebaseAuth.getInstance()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val types = resources.getStringArray(R.array.types)
        val dao = ProductDatabase.getInstance(application).productDAO
         repository = ProductRepository(dao)
        val factory = ProductViewModelFactory(repository,this)
        productViewModel = ViewModelProvider(this,factory)[ProductViewModel::class.java]

        binding.productViewModel = productViewModel
        val typeSpinner = binding.value
        val typeAdapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,
        resources.getStringArray(R.array.types))
        typeSpinner.adapter = typeAdapter
        typeSpinner.onItemSelectedListener = object :
        AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
               productViewModel.type = types[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        binding.signOutBtn.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.lifecycleOwner = this

        initRecyclerView()

    }

    private fun initRecyclerView() {
        binding.allProducts.layoutManager = LinearLayoutManager(this)
        displayProductsList()
    }

    private fun displayProductsList() {
        productViewModel.products.observe(this) {
            binding.allProducts.adapter = ProductRecyclerViewAdapter(it, repository) { selectedItem: Product ->
                listItemClicked(
                    selectedItem
                )
            }
        }
    }

    private fun listItemClicked(selectedItem: Product) {
        Toast.makeText(this,"Selected product is ${selectedItem.productName}",Toast.LENGTH_LONG).show()
        productViewModel.initUpdateAndDelete(selectedItem)

    }

}