package com.example.kitabu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kitabu.ui.theme.KitabuTheme
import com.example.kitabu.ui.theme.MainBackground
import com.example.kitabu.viewModel.BookViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KitabuTheme {
                val navController = rememberNavController()

                window.navigationBarColor = MainBackground.toArgb()
                window.statusBarColor = MainBackground.toArgb()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    KitabuApp(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun KitabuApp(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    bookViewModel: BookViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = "Books",
        modifier = modifier
    ) {
        composable("Books") {
            BooksScreen(
                navController = navController,
                viewModel = bookViewModel
            )
        }

        composable("UserBooks") {
            MyBooksSection(
                navController = navController,
                viewModel = bookViewModel
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KitabuTheme {
        KitabuApp(navController = NavHostController(LocalContext.current), modifier = Modifier)
    }
}
