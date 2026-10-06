package com.example.kitabu

import android.R.attr.tint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.kitabu.ui.theme.PrimaryPurple
import com.example.kitabu.ui.theme.TextMuted

@Composable
fun KitabuNavBar(navController: NavController, items : List<NavigationItems>){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // 1. The crisp separator line
    HorizontalDivider(
        thickness = 1.dp,
        color = Color.LightGray.copy(alpha = 0.4f)
    )

    // Use NavigationBar directly without the Surface wrapper
    NavigationBar(
        modifier = Modifier.fillMaxWidth().height(105.dp).navigationBarsPadding(), // Anchors to the edges
        containerColor = Color.White,
        tonalElevation = 8.dp, // Gives it a slight shadow
        // Notice: NO .navigationBarsPadding() here! Material 3 handles it automatically
        //windowInsets = NavigationBarDefaults.windowInsets
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            // Check if this specific item is the upload action
            // Make sure "educator_upload" matches whatever you named the route in NavigationItems!

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    // Standard styling for all other icons
                    Icon(
                        painter = if (isSelected) painterResource(id = item.SelectedIcon) else painterResource(id = item.UnselectedIcon),
                        contentDescription = item.name,
                        modifier = Modifier.size(24.dp),
                    )
                },
                label = {
                    // Only show text if it is NOT the upload button
                    Text(
                        text = item.name,
                        fontSize = 10.sp,
                        maxLines = 1,
                        softWrap = false,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryPurple,
                    selectedTextColor = PrimaryPurple,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}