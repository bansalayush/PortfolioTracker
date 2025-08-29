package com.scorpio.portfoliotracker.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.scorpio.portfoliotracker.data.local.HoldingDao
import com.scorpio.portfoliotracker.data.local.HoldingEntity

@Database(entities = [HoldingEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract
    fun
        holdingDao()
    :
        HoldingDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private const val DATABASE_NAME = "portfolio" // Database file name

        fun getDatabase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DATABASE_NAME,
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                Companion.instance = instance
                // return instance
                instance
            }
        }
    }
}
