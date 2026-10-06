package com.example.kitabu.data

data class BookingWithBook(
    val booking: BookingEntity,
    val book: BookEntity
)

data class HistoryWithBook(
    val history: BookingHistoryEntity,
    val book: BookEntity
)
