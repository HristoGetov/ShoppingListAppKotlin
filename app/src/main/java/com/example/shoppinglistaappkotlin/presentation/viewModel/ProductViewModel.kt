package com.example.shoppinglistaappkotlin.presentation.viewModel


import android.content.Context
import android.text.Html
import androidx.appcompat.app.AlertDialog
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglistaappkotlin.data.room.Product
import com.example.shoppinglistaappkotlin.data.room.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository, private val context: Context):ViewModel(), Observable {


    val products = repository.products
    private var isUpdateOrDelete = false
    private lateinit var productToUpdateOrDelete: Product
    private lateinit var builder: AlertDialog.Builder

    @Bindable
    var inputProductName = MutableLiveData<String>()
    @Bindable
    var inputQty = MutableLiveData<String>()
    @Bindable
    var inputType = MutableLiveData<String>()
    @Bindable
    var type:String = ""

    @Bindable
    val saveOrUpdateButtonText = MutableLiveData<String>()
    @Bindable
    val clearOrDeleteButton = MutableLiveData<String>()

    init {
        saveOrUpdateButtonText.value = "Запази"
        clearOrDeleteButton.value = "Изтрий всички"
    }



    fun saveOrUpdate(){
        if (isUpdateOrDelete){
            productToUpdateOrDelete.productName = inputProductName.value!!
            productToUpdateOrDelete.quantity = inputQty.value!!
           // productToUpdateOrDelete.type = inputType.value!!
            update(productToUpdateOrDelete)

        }else{
            val product = inputProductName.value!!
            val quantity = inputQty.value!!
            val type = type

            insert(Product(0,product,quantity,type))

            inputProductName.value = ""
            inputQty.value = ""
           // inputType.value = ""
        }

    }

    fun clearAllOrDelete(){
        if (isUpdateOrDelete) {

            builder = AlertDialog.Builder(context)
            builder.setTitle(Html.fromHtml("<b>Изтриване на продукт!</b>"))
            builder.setMessage("Сигурни ли сте, че искате да изтриете продуктa?")
            builder.setPositiveButton("Не"){ dialog, which ->

            }
            builder.setNegativeButton("Да"){dialog,which ->
                CoroutineScope(Dispatchers.IO).launch {
                    deleteOneItem(productToUpdateOrDelete)
                }
            }
            val alertDialog = builder.create()
            alertDialog.show()
            alertDialog.window?.setBackgroundDrawableResource(android.R.color.holo_orange_light)
        }else{
            builder = AlertDialog.Builder(context)
            builder.setTitle(Html.fromHtml("<b>Изтриване на всички продукти!</b>"))
            builder.setMessage("Сигурни ли сте, че искате да изтриете всички продукти?")
            builder.setPositiveButton("Не"){ dialog, which ->

            }
            builder.setNegativeButton("Да"){dialog,which ->
                CoroutineScope(Dispatchers.IO).launch {
                    clearAll()
                }
            }
            val alertDialog = builder.create()
            alertDialog.show()
            alertDialog.window?.setBackgroundDrawableResource(android.R.color.holo_orange_light)

        }
    }

    fun update(product: Product) = viewModelScope.launch {
        repository.update(product)
        inputProductName.value = ""
        inputQty.value = ""
        //inputType.value = ""
        isUpdateOrDelete = false
        saveOrUpdateButtonText.value = "Запази"
        clearOrDeleteButton.value = "Изтрий всички"
    }

    fun deleteOneItem(product: Product) = viewModelScope.launch {
        repository.delete(product)

        inputProductName.value = ""
        inputQty.value = ""
        //inputType.value = ""
        isUpdateOrDelete = false
        saveOrUpdateButtonText.value = "Запази"
        clearOrDeleteButton.value = "Изтрий всички"
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
        inputProductName.value = selectedItem.productName
        inputQty.value = selectedItem.quantity
        //inputType.value = selectedItem.type
        isUpdateOrDelete = true
        productToUpdateOrDelete = selectedItem
        saveOrUpdateButtonText.value = "Запази"
        clearOrDeleteButton.value = "Изтрий"
    }
}