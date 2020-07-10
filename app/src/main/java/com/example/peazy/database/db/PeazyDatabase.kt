package com.example.peazy.database.db

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.peazy.utility.Constants

//@Database(entities = [Subscriber::class], version = 1)
abstract class PeazyDatabase : RoomDatabase(){
    companion object {
        @Volatile
        private var INSTANCE: PeazyDatabase? = null

        fun getInstance(context: Context): PeazyDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PeazyDatabase::class.java,
                        Constants.DATABASE_NAME
                    ).build()
                }
                return instance
            }

        }
    }

}