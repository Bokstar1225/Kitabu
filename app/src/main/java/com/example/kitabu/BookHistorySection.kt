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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kitabu.data.HistoryWithBook
import com.example.kitabu.ui.BookCoverView
import com.example.kitabu.ui.theme.MainBackground
import com.example.kitabu.viewModel.BookViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BookHistorySection(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: BookViewModel = viewModel()
) {
    val historyList by viewModel.bookingHistory.collectAsState()

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
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MainBackground)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Column {
                Text(
                    text = "Book History",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Log of your past book rentals and activity.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (historyList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No rental history available yet.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(historyList) { item ->
                        HistoryCard(item = item)
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryCard(item: HistoryWithBook) {
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()) }
    val formattedDate = dateFormat.format(Date(item.history.actionDate))

    val badgeBg = when (item.history.actionType) {
        "RENTED" -> Color(0xFFEDE9FE)
        "RENEWED" -> Color(0xFFDBEAFE)
        "RETURNED" -> Color(0xFFDCFCE7)
        "CANCELLED" -> Color(0xFFFEE2E2)
        else -> Color(0xFFF1F5F9)
    }

    val badgeText = when (item.history.actionType) {
        "RENTED" -> Color(0xFF6D28D9)
        "RENEWED" -> Color(0xFF1D4ED8)
        "RETURNED" -> Color(0xFF15803D)
        "CANCELLED" -> Color(0xFFDC2626)
        else -> Color(0xFF475569)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BookCoverView(
                coverKey = item.book.coverImage,
                title = item.book.title,
                author = item.book.author,
                modifier = Modifier.size(width = 60.dp, height = 80.dp)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.book.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Box(
                        modifier = Modifier
                            .background(badgeBg, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = item.history.actionType,
                            style = MaterialTheme.typography.labelSmall,
                            color = badgeText,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.book.author,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookHistorySectionPreview() {
    if (!androidx.compose.ui.platform.LocalInspectionMode.current) {
        BookHistorySection(navController = NavController(LocalContext.current), modifier = Modifier)
    }
}
