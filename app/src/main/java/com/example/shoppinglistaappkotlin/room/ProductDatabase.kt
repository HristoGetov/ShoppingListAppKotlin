package com.example.shoppinglistaappkotlin.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Product::class], version = 1)
abstract class ProductDatabase: RoomDatabase() {
    abstract val productDAO: ProductDAO

    companion object{
        @Volatile
        private var INSTANCE: ProductDatabase ?= null
        fun getInstance(context: Context): ProductDatabase{
            synchronized(this){
                var instance = INSTANCE
                if (instance == null){
                    //Creating the database object
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ProductDatabase::class.java,
                        "products_db"
                    ).build()
                }
                return instance
            }
        }
    }
}