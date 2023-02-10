package com.example.camera_new_way

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDao {
    @Insert
    fun addProduct(product: Product)

    @Query("SELECT * FROM Product")
    fun getAllProduct(): List<Product>
}