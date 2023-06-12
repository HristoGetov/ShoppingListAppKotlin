package com.example.shoppinglistaappkotlin.room

class ProductRepository(private val dao: ProductDAO) {

    val products = dao.getAllProducts()

    suspend fun insert(product: Product):Long{
        return dao.insertProduct(product)
    }

    suspend fun delete(product: Product){
        dao.deleteProduct(product)
    }

    suspend fun update(product: Product){
        dao.updateProduct(product)
    }

    suspend fun deleteAll(){
        dao.deleteAll()
    }
}