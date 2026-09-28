package com.example.kitabu.data

class BookRepository(private val bookDao: BookDao){
    suspend fun getAllBooks(): List<BookEntity> {
        return bookDao.getAllBooks()
    }

    suspend fun getBookById(bookId: Int): BookEntity? {
        return bookDao.getBookById(bookId)
    }

    suspend fun getAvailableBooks(): List<BookEntity> {
        return bookDao.getAvailableBooks()
    }

    suspend fun getRentedBooks(): List<BookEntity> {
        return bookDao.getRentedBooks()
    }

    suspend fun getBooksByCategory(category: String): List<BookEntity> {
        return bookDao.getBooksByCategory(category)
    }

    suspend fun insertBook(book: BookEntity) {
        return bookDao.insertBook(book)
    }

    suspend fun insertBooking(booking: BookingEntity){
        return bookDao.insertBooking(booking)
    }

    suspend fun getAllBookings(): List<BookingEntity>{
        return bookDao.getAllBookings()
    }

    suspend fun getBookingByBookId(bookId: Int): BookingEntity?{
        return bookDao.getBookingByBookId(bookId)
    }

    suspend fun updateBook(book: BookEntity) {
        return bookDao.updateBook(book)
    }

    suspend fun updateBooking(booking: BookingEntity){
        return bookDao.updateBooking(booking)
    }

    suspend fun deleteBook(book: BookEntity) {
        return bookDao.deleteBook(book)
    }

    suspend fun deleteBooking(booking: BookingEntity){
        return bookDao.deleteBooking(booking)
    }
}