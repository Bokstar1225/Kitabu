package com.example.kitabu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.LibraryBooks
import androidx.compose.ui.graphics.vector.ImageVector


sealed class NavigationItems(val name: String, val route: String, val SelectedIcon: ImageVector, val UnselectedIcon: ImageVector){
    data object Books : NavigationItems("Books", "Books", Icons.Filled.Book, Icons.Outlined.Book)
    data object UserBooks : NavigationItems("My Books", "UserBooks", Icons.Filled.LibraryBooks, Icons.Outlined.LibraryBooks)
    data object BookHistory : NavigationItems("Book History", "BookHistory", Icons.Filled.Bookmarks, Icons.Outlined.Bookmarks)
}
