package com.example.moviedb.queryDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [QueryItem::class], version = 1, exportSchema = false)
abstract class QueryDatabase : RoomDatabase() {

    abstract val queryDAO: QueryDAO

    companion object {

        @Volatile
        private var INSTANCE: QueryDatabase? = null

        fun getInstance(context: Context): QueryDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        QueryDatabase::class.java,
                        "query_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
