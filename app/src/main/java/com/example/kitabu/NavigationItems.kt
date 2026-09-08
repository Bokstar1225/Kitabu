package com.example.kitabu


sealed class NavigationItems(val name: String, val route: String, val SelectedIcon: Int, val UnselectedIcon: Int){
    data object Books : NavigationItems("Books", "Books", R.drawable.icons8_catalog_24, R.drawable.icons8_catalog_24)
    data object UserBooks : NavigationItems("My Books", "UserBooks", R.drawable.icons8_books_24, R.drawable.icons8_books_24)
    data object BookHistory : NavigationItems("Book History", "BookHistory", R.drawable.icons8_book_24, R.drawable.icons8_book_24,)
}
