package com.example.camera_new_way

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(var name: String, var price: Int, var img: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
