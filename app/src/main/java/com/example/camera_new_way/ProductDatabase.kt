package com.example.camera_new_way

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Product::class], version = 1)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        private var instance: ProductDatabase? = null

        @Synchronized
        fun getInstance(context: Context): ProductDatabase {
            if (instance == null)
                instance = Room
                    .databaseBuilder(context, ProductDatabase::class.java, "product_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()

            return instance!!
        }

    }
}