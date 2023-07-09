package com.example.shoppinglistaappkotlin.presentation

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
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
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout


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

        drawerLayout = binding.drawerLayout

        toggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open,
            R.string.close
        )

        drawerLayout.addDrawerListener(toggle)

        //synchronize the state of the drawer Open/Close
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.productViewModel = productViewModel
        val navView = binding.navView
        navView.setNavigationItemSelectedListener {
            it.isChecked = true
            when (it.itemId) {
                R.id.logout ->{
                    firebaseAuth.signOut()
                    val intent = Intent(this, SignInActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            true
        }

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


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}