package com.example.kitabu.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.kitabu.data.AppDatabase
import com.example.kitabu.data.BookEntity
import com.example.kitabu.data.BookRepository
import com.example.kitabu.data.BookingEntity
import com.example.kitabu.data.BookingStatus
import com.example.kitabu.data.BookingWithBook
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
        }
    }

    fun reserveBook(book: BookEntity, returnDeadlineMillis: Long, userName: String = "User") {
        viewModelScope.launch {
            val booking = BookingEntity(
                bookOwnerId = book.id,
                userName = userName,
                bookingDate = System.currentTimeMillis(),
                returnDeadline = returnDeadlineMillis,
                status = BookingStatus.ACTIVE
            )
            repository.insertBooking(booking)
            repository.updateBook(book.copy(rentStatus = true))
            refreshData()
        }
    }

    fun cancelBooking(bookingWithBook: BookingWithBook) {
        viewModelScope.launch {
            repository.deleteBooking(bookingWithBook.booking)
            repository.updateBook(bookingWithBook.book.copy(rentStatus = false))
            refreshData()
        }
    }

    fun renewBooking(bookingWithBook: BookingWithBook, additionalDays: Int = 7) {
        viewModelScope.launch {
            val updatedDeadline = bookingWithBook.booking.returnDeadline + (additionalDays * 24 * 60 * 60 * 1000L)
            repository.updateBooking(bookingWithBook.booking.copy(returnDeadline = updatedDeadline))
            refreshData()
        }
    }

    fun returnBook(bookingWithBook: BookingWithBook) {
        viewModelScope.launch {
            repository.deleteBooking(bookingWithBook.booking)
            repository.updateBook(bookingWithBook.book.copy(rentStatus = false))
            refreshData()
        }
    }
}
