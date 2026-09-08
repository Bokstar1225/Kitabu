package com.example.kitabu.data

import androidx.room3.Database
import androidx.room3.RoomDatabase

@Database(entities = [BookEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}