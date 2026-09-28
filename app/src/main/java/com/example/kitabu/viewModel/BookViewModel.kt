package com.example.kitabu.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.kitabu.data.AppDatabase
import com.example.kitabu.data.BookEntity
import com.example.kitabu.data.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: BookRepository

    private val _allBooks = MutableStateFlow<List<BookEntity>>(emptyList())
    val allBooks: StateFlow<List<BookEntity>> = _allBooks.asStateFlow()

    init {
        val bookDao = AppDatabase.getDatabase(application).bookDao()
        repository = BookRepository(bookDao)
        loadBooks()
    }

    fun loadBooks() {
        viewModelScope.launch {
            _allBooks.value = repository.getAllBooks()
        }
    }
}
