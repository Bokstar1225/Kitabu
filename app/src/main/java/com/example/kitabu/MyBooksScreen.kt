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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kitabu.data.BookingStatus
import com.example.kitabu.data.BookingWithBook
import com.example.kitabu.ui.BookCoverView
import com.example.kitabu.ui.theme.MainBackground
import com.example.kitabu.ui.theme.PrimaryDarkPurple
import com.example.kitabu.viewModel.BookViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBooksSection(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: BookViewModel = viewModel()
) {
    val bookingsWithBooks by viewModel.userBookings.collectAsState()

    var selectedBookingForRenew by remember { mutableStateOf<BookingWithBook?>(null) }
    var showRenewBottomSheet by remember { mutableStateOf(false) }

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
                    text = "My Books",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Manage your current reservations.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (bookingsWithBooks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "You have no active reservations.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(bookingsWithBooks) { item ->
                        ReservationCard(
                            item = item,
                            onRenew = {
                                selectedBookingForRenew = item
                                showRenewBottomSheet = true
                            },
                            onReturn = { viewModel.returnBook(item) },
                            onCancel = { viewModel.cancelBooking(item) }
                        )
                    }
                }
            }
        }

        // Renew Reservation Calendar Modal Bottom Sheet
        if (showRenewBottomSheet && selectedBookingForRenew != null) {
            val item = selectedBookingForRenew!!
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            val currentDeadline = item.booking.returnDeadline
            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = currentDeadline)

            ModalBottomSheet(
                onDismissRequest = { showRenewBottomSheet = false },
                sheetState = sheetState,
                containerColor = Color.White,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        BookCoverView(
                            coverKey = item.book.coverImage,
                            title = item.book.title,
                            author = item.book.author,
                            modifier = Modifier.size(width = 80.dp, height = 105.dp)
                        )

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.book.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.book.author,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFE0E0FF), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = item.book.category,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = PrimaryDarkPurple,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Renew Reservation",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Select a new deadline date on the calendar to extend your rental.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Embedded Calendar
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            val newDeadline = datePickerState.selectedDateMillis ?: currentDeadline
                            viewModel.renewBooking(item, newDeadline)
                            showRenewBottomSheet = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryDarkPurple)
                    ) {
                        Text(
                            text = "Confirm Renewal",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun ReservationCard(
    item: BookingWithBook,
    onRenew: () -> Unit,
    onReturn: () -> Unit,
    onCancel: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }
    val reservedDateStr = dateFormat.format(Date(item.booking.bookingDate))
    val dueDateStr = dateFormat.format(Date(item.booking.returnDeadline))

    val diffMillis = item.booking.returnDeadline - System.currentTimeMillis()
    val daysLeft = maxOf(0L, diffMillis / (1000 * 60 * 60 * 24))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Book Cover Box
                BookCoverView(
                    coverKey = item.book.coverImage,
                    title = item.book.title,
                    author = item.book.author,
                    modifier = Modifier.size(width = 68.dp, height = 90.dp)
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

                        // Status Badge
                        val badgeBg = when (item.booking.status) {
                            BookingStatus.ACTIVE -> Color(0xFFEDE9FE)
                            BookingStatus.PENDING -> Color(0xFFFFEDD5)
                            BookingStatus.RETURNED -> Color(0xFFDCFCE7)
                        }
                        val badgeText = when (item.booking.status) {
                            BookingStatus.ACTIVE -> Color(0xFF6D28D9)
                            BookingStatus.PENDING -> Color(0xFFC2410C)
                            BookingStatus.RETURNED -> Color(0xFF15803D)
                        }

                        Box(
                            modifier = Modifier
                                .background(badgeBg, RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = item.booking.status.name,
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

                    Spacer(modifier = Modifier.height(10.dp))

                    // Date Row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Reserved: $reservedDateStr",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Due: $dueDateStr",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "$daysLeft days left",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF3B82F6),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons Row (Renew, Return, Cancel)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onRenew,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Renew",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Renew", style = MaterialTheme.typography.labelMedium)
                }

                OutlinedButton(
                    onClick = onReturn,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Return",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Return", style = MaterialTheme.typography.labelMedium)
                }

                Button(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE2E2))
                ) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = "Cancel",
                        tint = Color(0xFFDC2626),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFFDC2626),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MyBooksSectionPreview() {
    if (!androidx.compose.ui.platform.LocalInspectionMode.current) {
        MyBooksSection(navController = NavController(LocalContext.current), modifier = Modifier)
    }
}
