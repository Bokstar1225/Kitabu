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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kitabu.ui.theme.MainBackground

@Composable
fun MyBooksSection(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    val myBooks = listOf(
        MyBook(
            title = "Things Fall Apart",
            author = "Chinua Achebe",
            progress = 0.65f,
            progressText = "65%",
            status = "Reading"
        ),
        MyBook(
            title = "Long Walk to Freedom",
            author = "Nelson Mandela",
            progress = 1.0f,
            progressText = "100%",
            status = "Completed"
        ),
        MyBook(
            title = "The Alchemist",
            author = "Paulo Coelho",
            progress = 0.20f,
            progressText = "20%",
            status = "Reading"
        )
    )

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
    ) {innerPadding ->
        Column(
            modifier = modifier.fillMaxWidth()
                .background(MainBackground)
                .padding(innerPadding)
                .padding(horizontal = 12.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "My Books",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                TextButton(
                    onClick = {
                        // Later: navigate to all My Books
                    }
                ) {
                    Text("See all")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(myBooks) { book ->

                    MyBookCard(book = book)
                }
            }
        }
    }
}

@Composable
fun MyBookCard(
    book: MyBook
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF4EFE6)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Book cover
            Box(
                modifier = Modifier
                    .size(width = 72.dp, height = 92.dp)
                    .background(
                        color = Color(0xFF6B705C),
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "BOOK",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = book.author,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Progress bar
                LinearProgressIndicator(
                    progress = { book.progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    color = Color(0xFF6B705C),
                    trackColor = Color(0xFFD9D4C9)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = book.status,
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF6B705C),
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = book.progressText,
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MyBooksSectionPreview(){
    MyBooksSection(navController = NavController(LocalContext.current), modifier = Modifier)
}