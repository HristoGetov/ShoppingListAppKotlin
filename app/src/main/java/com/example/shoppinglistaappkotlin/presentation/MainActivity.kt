package com.example.shoppinglistaappkotlin.presentation

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppinglistaappkotlin.R
import com.example.shoppinglistaappkotlin.data.DataStoreManager
import com.example.shoppinglistaappkotlin.presentation.view.ProductRecyclerViewAdapter
import com.example.shoppinglistaappkotlin.presentation.viewModel.ProductViewModel
import com.example.shoppinglistaappkotlin.presentation.viewModel.ProductViewModelFactory
import com.example.shoppinglistaappkotlin.databinding.ActivityMainBinding
import com.example.shoppinglistaappkotlin.data.room.Product
import com.example.shoppinglistaappkotlin.data.room.ProductDatabase
import com.example.shoppinglistaappkotlin.data.room.ProductRepository
import com.example.shoppinglistaappkotlin.presentation.viewModel.DataStoreViewModel
import com.example.shoppinglistaappkotlin.presentation.viewModel.FindFriendsActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import java.util.Locale
import java.util.prefs.Preferences

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var  productViewModel: ProductViewModel
    private lateinit var repository: ProductRepository
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var context: Context
    private lateinit var dataStoreViewModel: DataStoreViewModel
    private lateinit var dataStoreManager: DataStoreManager




    companion object{
        private lateinit var instance: MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this
        context = this
        firebaseAuth = FirebaseAuth.getInstance()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        dataStoreViewModel = ViewModelProvider(this)[DataStoreViewModel::class.java]
        dataStoreManager = DataStoreManager(this)
        checkLanguage()
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
                R.id.find_people ->{
                    val intent = Intent(this,FindFriendsActivity::class.java)
                    startActivity(intent)
                }
                R.id.friends_list ->{
                    val intent = Intent(this, FriendsListActivity::class.java)
                    startActivity(intent)
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
        binding.langSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            Log.e("Log", "isChecked: $isChecked")
            if (isChecked){
                setLocale("en-En")
            }else{
                setLocale("bg-BG")
            }
        })
        binding.lifecycleOwner = this
        initRecyclerView()
        binding.langSwitch.setOnCheckedChangeListener{ _ , isChecked ->
            when(isChecked){
                true->{
                    dataStoreViewModel.setLanguage(false)
                }false ->{
                dataStoreViewModel.setLanguage(true)
                }
            }
        }
    }

    private fun checkLanguage() {
        binding.apply {
            dataStoreViewModel.getLanguage.observe(this@MainActivity) { isBGLannguage ->
                when (isBGLannguage) {
                    true -> {
                        setLocale("bg-BG")
                        langSwitch.isChecked = false
                    }
                    false -> {
                        setLocale("en-EN")
                        langSwitch.isChecked = true
                    }
                }
            }
        }

    }
    private fun setLocale(language: String?) {
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(language)
        AppCompatDelegate.setApplicationLocales(appLocale)
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