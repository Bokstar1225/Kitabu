package com.example.kitabu.data

class BookRepository(private val bookDao: BookDao) {
    suspend fun getAllBooks(): List<BookEntity> = bookDao.getAllBooks()

    suspend fun getAvailableBooks(): List<BookEntity> = bookDao.getAvailableBooks()

    suspend fun insertBook(book: BookEntity) = bookDao.insertBook(book)

    suspend fun updateBook(book: BookEntity) = bookDao.updateBook(book)

    suspend fun deleteBook(book: BookEntity) = bookDao.deleteBook(book)

    suspend fun insertBooking(booking: BookingEntity) = bookDao.insertBooking(booking)

    suspend fun insertBookingHistory(history: BookingHistoryEntity) = bookDao.insertBookingHistory(history)

    suspend fun getAllBookings(): List<BookingEntity> = bookDao.getAllBookings()

    suspend fun getAllBookingHistory(): List<BookingHistoryEntity> = bookDao.getAllBookingHistory()

    suspend fun getBookingByBookId(bookId: Int): BookingEntity? = bookDao.getBookingByBookId(bookId)

    suspend fun updateBooking(booking: BookingEntity) = bookDao.updateBooking(booking)

    suspend fun deleteBooking(booking: BookingEntity) = bookDao.deleteBooking(booking)

    suspend fun getBookingsWithBooks(): List<BookingWithBook> {
        val bookings = bookDao.getAllBookings()
        val books = bookDao.getAllBooks().associateBy { it.id }
        return bookings.mapNotNull { booking ->
            books[booking.bookOwnerId]?.let { book ->
                BookingWithBook(booking = booking, book = book)
            }
        }
    }

    suspend fun getBookingHistoryWithBooks(): List<HistoryWithBook> {
        val histories = bookDao.getAllBookingHistory()
        val books = bookDao.getAllBooks().associateBy { it.id }
        return histories.mapNotNull { history ->
            books[history.bookOwnerId]?.let { book ->
                HistoryWithBook(history = history, book = book)
            }
        }
    }
}
