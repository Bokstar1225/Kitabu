package com.example.kitabu.data

import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(
    tableName = "books",
    indices = [Index(value = ["title"], unique = true)]
)
data class BookEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val author: String,
    val category: String,
    val rentStatus: Boolean = false,
    val coverImage: String = ""
)
