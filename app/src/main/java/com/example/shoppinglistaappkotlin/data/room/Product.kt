package com.example.shoppinglistaappkotlin.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class Product(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    val id:Int,

    @ColumnInfo(name = "product_name")
    var productName: String,

    @ColumnInfo(name = "qty")
    var quantity: String,

    @ColumnInfo(name = "product_type")
    var type: String
)
