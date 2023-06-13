package com.example.shoppinglistaappkotlin.ViewModel


import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglistaappkotlin.room.Product
import com.example.shoppinglistaappkotlin.room.ProductRepository
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository):ViewModel(), Observable {

    val products = repository.products
    private var isUpdateOrDelete = false
    private lateinit var productToUpdateOrDelete: Product

    @Bindable
    val inputProductName = MutableLiveData<String>()
    @Bindable
    val inputQty = MutableLiveData<String>()
    @Bindable
    val inputType = MutableLiveData<String>()

    @Bindable
    val saveOrUpdateButtonText = MutableLiveData<String>()
    @Bindable
    val clearOrDeleteButton = MutableLiveData<String>()

    init {
        saveOrUpdateButtonText.value = "Save"
        clearOrDeleteButton.value = "Clear All"
    }

    fun saveOrUpdate(){
        val product = inputProductName.value!!
        val quantity = inputQty.value!!
        val type = inputType.value!!

        insert(Product(0,product,quantity,type))

        inputProductName.value = ""
        inputQty.value = ""
        inputType.value = ""
    }

    fun clearAllOrDelete(){
        clearAll()
    }

    fun update(product: Product) = viewModelScope.launch {
        repository.update(product)
    }

    fun deleteOneItem(product: Product) = viewModelScope.launch {
        repository.delete(product)
    }

    private fun clearAll()=viewModelScope.launch {
        repository.deleteAll()
    }

    private fun insert(product: Product) = viewModelScope.launch {
        repository.insert(product)
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    fun initUpdateAndDelete(selectedItem: Product) {

    }
}