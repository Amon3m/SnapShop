package com.itigradteamsix.snapshop.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.itigradteamsix.snapshop.model.Product

abstract class SnapShopDatabase : RoomDatabase() {
    abstract fun snapShopDao(): SnapShopDao

    companion object {
        @Volatile
        private var INSTANCE: SnapShopDatabase? = null

        fun getDatabase(context: Context): SnapShopDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SnapShopDatabase::class.java,
                    "snap_shop_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
