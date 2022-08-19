package com.mp.arduinobluetooth.core.data.source.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mp.arduinobluetooth.core.data.source.local.entity.Command

@Database(entities = [Command::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase()  {

    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "command_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}