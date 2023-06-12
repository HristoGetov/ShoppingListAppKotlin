package com.example.shoppinglistaappkotlin.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class Product(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    val id:Int,

    @ColumnInfo(name = "product_name")
    val productName: String,

    @ColumnInfo(name = "qty")
    val quantity: String,

    @ColumnInfo(name = "product_type")
    val type: String
)
