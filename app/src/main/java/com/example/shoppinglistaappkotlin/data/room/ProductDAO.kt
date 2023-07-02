package com.example.shoppinglistaappkotlin.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductDAO {

    @Insert
    suspend fun insertProduct(product: Product) :Long

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("DELETE FROM product")
    suspend fun deleteAll()

    @Query("SELECT * FROM product")
    fun getAllProducts(): LiveData<List<Product>>
}