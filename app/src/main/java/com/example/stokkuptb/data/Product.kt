package com.example.stokkuptb.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val category: String,
    val stock: Int,
    val price: Double,
    val imageUri: String? = null 
)