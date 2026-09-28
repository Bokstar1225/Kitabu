package com.example.kitabu.data

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey

enum class BookingStatus {
    PENDING,
    ACTIVE,
    RETURNED
}

@Entity(
    tableName = "book_reservations",
    foreignKeys = [
        ForeignKey(
            entity = BookEntity::class,
            parentColumns = ["id"],
            childColumns = ["bookOwnerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["bookOwnerId"])]
)
data class BookingEntity(
    @PrimaryKey(autoGenerate = true)
    val bookingId: Int = 0,
    val bookOwnerId: Int,
    val userName: String,
    val bookingDate: Long,
    val returnDeadline: Long,
    val status: BookingStatus
)
