package com.commityourself.gitmobile.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.commityourself.gitmobile.database.models.Repository

@Database(entities = [Repository::class], version = 1)
abstract class RepositoryDatabase : RoomDatabase() {

    abstract fun repositoryDao(): RepositoryDao

    /*companion object {
        @Volatile
        private var INSTANCE: RepositoryDatabase? = null

        fun getDatabase(context: Context): RepositoryDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RepositoryDatabase::class.java,
                    "Word_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }*/
}