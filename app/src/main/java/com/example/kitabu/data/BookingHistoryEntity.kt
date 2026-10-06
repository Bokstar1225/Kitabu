package com.example.kitabu.data

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "booking_history")
data class BookingHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val bookOwnerId: Int,
    val userName: String,
    val actionType: String = "RENTED",
    val actionDate: Long = System.currentTimeMillis()
)
