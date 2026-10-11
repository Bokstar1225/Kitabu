package com.example.kitabu.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.kitabu.data.AppDatabase
import com.example.kitabu.data.BookEntity
import com.example.kitabu.data.BookRepository
import com.example.kitabu.data.BookingEntity
import com.example.kitabu.data.BookingHistoryEntity
import com.example.kitabu.data.BookingStatus
import com.example.kitabu.data.BookingWithBook
import com.example.kitabu.data.HistoryWithBook
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: BookRepository

    private val _allBooks = MutableStateFlow<List<BookEntity>>(emptyList())
    val allBooks: StateFlow<List<BookEntity>> = _allBooks.asStateFlow()

    private val _userBookings = MutableStateFlow<List<BookingWithBook>>(emptyList())
    val userBookings: StateFlow<List<BookingWithBook>> = _userBookings.asStateFlow()

    private val _bookingHistory = MutableStateFlow<List<HistoryWithBook>>(emptyList())
    val bookingHistory: StateFlow<List<HistoryWithBook>> = _bookingHistory.asStateFlow()

    init {
        val bookDao = AppDatabase.getDatabase(application).bookDao()
        repository = BookRepository(bookDao)
        refreshData()
    }

    fun refreshData() {
        viewModelScope.launch {
            var books = repository.getAllBooks()
            if (books.isEmpty()) {
                val initialBooks = listOf(
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
                initialBooks.forEach { repository.insertBook(it) }
                books = repository.getAllBooks()
            }
            _allBooks.value = books
            _userBookings.value = repository.getBookingsWithBooks()
            _bookingHistory.value = repository.getBookingHistoryWithBooks()
        }
    }

    fun reserveBook(
        book: BookEntity,
        bookingDateMillis: Long,
        returnDeadlineMillis: Long,
        userName: String = "User"
    ) {
        viewModelScope.launch {
            val booking = BookingEntity(
                bookOwnerId = book.id,
                userName = userName,
                bookingDate = bookingDateMillis,
                returnDeadline = returnDeadlineMillis,
                status = BookingStatus.PENDING
            )
            repository.insertBooking(booking)
            repository.insertBookingHistory(
                BookingHistoryEntity(
                    bookOwnerId = book.id,
                    userName = userName,
                    actionType = "RESERVED (PENDING)",
                    actionDate = System.currentTimeMillis()
                )
            )
            repository.updateBook(book.copy(rentStatus = true))
            refreshData()
        }
    }

    fun activateBooking(bookingWithBook: BookingWithBook) {
        viewModelScope.launch {
            val updatedBooking = bookingWithBook.booking.copy(status = BookingStatus.ACTIVE)
            repository.updateBooking(updatedBooking)
            repository.insertBookingHistory(
                BookingHistoryEntity(
                    bookOwnerId = bookingWithBook.book.id,
                    userName = bookingWithBook.booking.userName,
                    actionType = "PICKED UP (ACTIVE)",
                    actionDate = System.currentTimeMillis()
                )
            )
            refreshData()
        }
    }

    fun cancelBooking(bookingWithBook: BookingWithBook) {
        viewModelScope.launch {
            repository.deleteBooking(bookingWithBook.booking)
            repository.insertBookingHistory(
                BookingHistoryEntity(
                    bookOwnerId = bookingWithBook.book.id,
                    userName = bookingWithBook.booking.userName,
                    actionType = "CANCELLED",
                    actionDate = System.currentTimeMillis()
                )
            )
            repository.updateBook(bookingWithBook.book.copy(rentStatus = false))
            refreshData()
        }
    }

    fun renewBooking(bookingWithBook: BookingWithBook, newDeadlineMillis: Long) {
        viewModelScope.launch {
            repository.updateBooking(bookingWithBook.booking.copy(returnDeadline = newDeadlineMillis))
            repository.insertBookingHistory(
                BookingHistoryEntity(
                    bookOwnerId = bookingWithBook.book.id,
                    userName = bookingWithBook.booking.userName,
                    actionType = "RENEWED",
                    actionDate = System.currentTimeMillis()
                )
            )
            refreshData()
        }
    }

    fun returnBook(bookingWithBook: BookingWithBook) {
        viewModelScope.launch {
            repository.deleteBooking(bookingWithBook.booking)
            repository.insertBookingHistory(
                BookingHistoryEntity(
                    bookOwnerId = bookingWithBook.book.id,
                    userName = bookingWithBook.booking.userName,
                    actionType = "RETURNED",
                    actionDate = System.currentTimeMillis()
                )
            )
            repository.updateBook(bookingWithBook.book.copy(rentStatus = false))
            refreshData()
        }
    }
}
