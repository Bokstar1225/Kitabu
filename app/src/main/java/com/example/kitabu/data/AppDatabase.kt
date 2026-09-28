package com.example.kitabu.data

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.SQLiteConnection

@Database(entities = [BookEntity::class, BookingEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kitabu_database"
                )
                    .addCallback(AppDatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class AppDatabaseCallback : RoomDatabase.Callback() {
            override suspend fun onCreate(connection: SQLiteConnection) {
                super.onCreate(connection)
                INSTANCE?.let { database ->
                    populateDatabase(database.bookDao())
                }
            }

            suspend fun populateDatabase(bookDao: BookDao) {
                bookDao.insertBook(
                    BookEntity(
                        title = "Things Fall Apart",
                        author = "Chinua Achebe",
                        category = "Fiction",
                        rentStatus = false,
                        coverImage = "things_fall_apart"
                    ),
                    BookEntity(
                        title = "The Alchemist",
                        author = "Paulo Coelho",
                        category = "Fiction",
                        rentStatus = false,
                        coverImage = "the_alchemist"
                    ),
                    BookEntity(
                        title = "Half of a Yellow Sun",
                        author = "Chimamanda Ngozi Adichie",
                        category = "Fiction",
                        rentStatus = false,
                        coverImage = "half_of_a_yellow_sun"
                    ),
                    BookEntity(
                        title = "Long Walk to Freedom",
                        author = "Nelson Mandela",
                        category = "Biography",
                        rentStatus = false,
                        coverImage = "long_walk_to_freedom"
                    ),
                    BookEntity(
                        title = "We Need New Names",
                        author = "NoViolet Bulawayo",
                        category = "Fiction",
                        rentStatus = false,
                        coverImage = "we_need_new_names"
                    )
                )
            }
        }
    }
}
