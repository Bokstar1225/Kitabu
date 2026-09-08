package com.example.kitabu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kitabu.ui.theme.MainBackground
import com.example.kitabu.ui.theme.PrimaryDarkPurple

data class Book(
    val title: String,
    val author: String,
    val category: String
)

@Composable
fun BooksScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {

    var searchText by remember {
        mutableStateOf("")
    }

    var selectedCategory by remember {
        mutableStateOf("All")
    }

    val books = listOf(
        Book(
            title = "Things Fall Apart",
            author = "Chinua Achebe",
            category = "Fiction"
        ),
        Book(
            title = "The Alchemist",
            author = "Paulo Coelho",
            category = "Fiction"
        ),
        Book(
            title = "Half of a Yellow Sun",
            author = "Chimamanda Ngozi Adichie",
            category = "Fiction"
        ),
        Book(
            title = "Long Walk to Freedom",
            author = "Nelson Mandela",
            category = "Biography"
        ),
        Book(
            title = "We Need New Names",
            author = "NoViolet Bulawayo",
            category = "Fiction"
        )
    )

    val categories = listOf(
        "All",
        "Fiction",
        "Biography",
        "History"
    )

    val filteredBooks = books.filter { book ->

        val matchesSearch =
            book.title.contains(searchText, ignoreCase = true) ||
                    book.author.contains(searchText, ignoreCase = true)

        val matchesCategory =
            selectedCategory == "All" ||
                    book.category == selectedCategory

        matchesSearch && matchesCategory
    }

    val screens = listOf(
        NavigationItems.Books,
        NavigationItems.UserBooks,
        NavigationItems.BookHistory
    )
    Scaffold(
        bottomBar = {
            KitabuNavBar(navController = navController, items = screens)
        },
        containerColor = Color.White
    ){innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MainBackground)
                .padding(horizontal = 20.dp)
                .padding(innerPadding)
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {
                    Text(
                        text = "Kitabu",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryDarkPurple
                    )
                }

                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings"
                )
            }

            Spacer(modifier = Modifier.height(27.dp))

            Column{
                Text(
                    text = "Discover books",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Find and reserve your next book",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search
            OutlinedTextField(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                value = searchText,
                onValueChange = {
                    searchText = it
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Search books or authors")
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Categories
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                categories.forEach { category ->

                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = {
                            selectedCategory = category
                        },
                        label = {
                            Text(category)
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryDarkPurple,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Section title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Book Catalog",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                TextButton(
                    onClick = {
                        searchText = ""
                        selectedCategory = "All"
                    }
                ) {
                    Text("View All")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Book list
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredBooks) { book ->
                    BookCard(
                        book = book,
                        onClick = {
                            // Navigate to details
                        }
                    )
                }
            }
        }
    }

}

@Composable
fun BookCard(
    book: Book,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,
        modifier = Modifier.width(180.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            // Cover
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(140.dp)
                    .background(
                        color = Color(0xFF6B705C),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "BOOK",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = book.title,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = book.author,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = book.category,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF6B705C)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BooksScreenPreview(){
    BooksScreen(navController = NavController(LocalContext.current), modifier = Modifier)
}