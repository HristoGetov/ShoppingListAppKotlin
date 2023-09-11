package com.example.shoppinglistaappkotlin.presentation.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.shoppinglistaappkotlin.data.DataStoreManager
import com.example.shoppinglistaappkotlin.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DataStoreViewModel(application: Application) : AndroidViewModel(application) {


    val dataStore = DataStoreManager(application)
    val getLanguage = dataStore.getLanguage().asLiveData(Dispatchers.IO)
    fun setLanguage(isBulgarian : Boolean){
        viewModelScope.launch {
            dataStore.setLanguage(isBulgarian)
        }
    }

}