package com.itigradteamsix.snapshop.data.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.itigradteamsix.snapshop.data.models.Product

//TODO ADD ENTITIES HERE []
@Database(entities = [Product::class], version = 1)
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
