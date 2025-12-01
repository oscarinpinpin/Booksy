package com.booksy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.booksy.data.models.Book
import com.booksy.data.models.GoogleBookItem
import com.booksy.data.remote.GoogleBooksClient
import com.booksy.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class BooksUiState {
    object Loading : BooksUiState()
    data class Success(val books: List<Book>) : BooksUiState()
    data class Error(val message: String) : BooksUiState()
}

sealed class GoogleBooksUiState {
    object Idle : GoogleBooksUiState()
    object Loading : GoogleBooksUiState()
    data class Success(val books: List<GoogleBookItem>) : GoogleBooksUiState()
    data class Error(val message: String) : GoogleBooksUiState()
}

class BooksViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<BooksUiState>(BooksUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("todas")
    val selectedCategory = _selectedCategory.asStateFlow()

    // estado para google books api externa
    private val _googleBooksState = MutableStateFlow<GoogleBooksUiState>(GoogleBooksUiState.Idle)
    val googleBooksState = _googleBooksState.asStateFlow()

    private val _googleQuery = MutableStateFlow("")
    val googleQuery = _googleQuery.asStateFlow()

    private var allBooks = listOf<Book>()

    init {
        loadBooks()
    }

    fun loadBooks() {
        viewModelScope.launch {
            _uiState.value = BooksUiState.Loading
            try {
                val response = RetrofitClient.api.getAllBooks()
                if (response.isSuccessful && response.body() != null) {
                    allBooks = response.body()!!
                    applyFilters()
                } else {
                    _uiState.value = BooksUiState.Error("error al cargar libros")
                }
            } catch (e: Exception) {
                _uiState.value = BooksUiState.Error("error de conexion")
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        applyFilters()
    }

    fun onCategoryChange(category: String) {
        _selectedCategory.value = category
        applyFilters()
    }

    // filtro por busqueda y categoria
    private fun applyFilters() {
        var filteredBooks = allBooks

        // buscar por titulo o autor
        if (_searchQuery.value.isNotBlank()) {
            filteredBooks = filteredBooks.filter {
                it.title.contains(_searchQuery.value, ignoreCase = true) ||
                it.author.contains(_searchQuery.value, ignoreCase = true)
            }
        }

        // filtrar por categoria
        if (_selectedCategory.value != "todas") {
            filteredBooks = filteredBooks.filter {
                it.category.equals(_selectedCategory.value, ignoreCase = true)
            }
        }

        _uiState.value = BooksUiState.Success(filteredBooks)
    }

    // buscar libros en google books api externa
    fun searchGoogleBooks(query: String) {
        if (query.isBlank()) {
            _googleBooksState.value = GoogleBooksUiState.Idle
            return
        }

        _googleQuery.value = query
        viewModelScope.launch {
            _googleBooksState.value = GoogleBooksUiState.Loading
            try {
                val response = GoogleBooksClient.api.searchBooks(query)
                if (response.isSuccessful && response.body() != null) {
                    val books = response.body()!!.items ?: emptyList()
                    _googleBooksState.value = GoogleBooksUiState.Success(books)
                } else {
                    _googleBooksState.value = GoogleBooksUiState.Error("error al buscar")
                }
            } catch (e: Exception) {
                _googleBooksState.value = GoogleBooksUiState.Error("error de conexion")
            }
        }
    }
}