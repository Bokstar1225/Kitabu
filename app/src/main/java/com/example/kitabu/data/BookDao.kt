package com.example.kitabu.data

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update

@Dao
interface BookDao {
    @Query("SELECT * FROM books")
    suspend fun getAllBooks(): List<BookEntity>

    @Query("SELECT * FROM books WHERE id = :bookId")
    suspend fun getBookById(bookId: Int): BookEntity?

    @Query("SELECT * FROM books WHERE rentStatus = false")
    suspend fun getAvailableBooks(): List<BookEntity>

    @Query("SELECT * FROM books WHERE category = :category")
    suspend fun getBooksByCategory(category: String): List<BookEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(vararg book: BookEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookingHistory(history: BookingHistoryEntity)

    @Query("SELECT * FROM book_reservations")
    suspend fun getAllBookings(): List<BookingEntity>

    @Query("SELECT * FROM booking_history ORDER BY actionDate DESC")
    suspend fun getAllBookingHistory(): List<BookingHistoryEntity>

    @Query("SELECT * FROM book_reservations WHERE bookOwnerId = :bookId")
    suspend fun getBookingByBookId(bookId: Int): BookingEntity?

    @Update
    suspend fun updateBook(vararg book: BookEntity)

    @Update
    suspend fun updateBooking(vararg booking: BookingEntity)

    @Delete
    suspend fun deleteBook(vararg book: BookEntity)

    @Delete
    suspend fun deleteBooking(vararg booking: BookingEntity)
}
