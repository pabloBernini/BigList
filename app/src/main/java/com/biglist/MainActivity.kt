package com.biglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.biglist.ui.screens.PostScreen
import com.biglist.ui.viewModels.HomeViewModel
import com.biglist.ui.viewModels.HomeViewModelFactory
import com.biglist.ui.theme.ListUsersTheme
import com.biglist.model.NavigationDestinations
import com.biglist.ui.screens.HomeScreen
import com.biglist.ui.screens.UserScreen


class MainActivity : ComponentActivity() {
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as BigListApplication
        val factory = HomeViewModelFactory(app.repository)
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        setContent {
            ListUsersTheme {
                AppScreen(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen(viewModel: HomeViewModel) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (currentRoute) {
                            NavigationDestinations.HOME_SCREEN -> "Users List"
                            NavigationDestinations.USER_SCREEN -> "User Details"
                            "${NavigationDestinations.POST_SCREEN}/{userId}" -> "Post Details"
                            else -> ""
                        }, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    if (currentRoute != NavigationDestinations.HOME_SCREEN) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                            )
                        }
                    } else {

                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary) // Use MaterialTheme colors
            )
        }) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavigationDestinations.HOME_SCREEN,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(
                NavigationDestinations.HOME_SCREEN
            ) {

                    backStackEntry ->
                HomeScreen(
                    viewModel = viewModel, onUserSelected = { user ->
                        navController.navigate(NavigationDestinations.USER_SCREEN)
                        viewModel.onUserSelected(user)
                    },
                    onPostSelected = { post ->
                        navController.navigate("${NavigationDestinations.POST_SCREEN}/${post.userId}")
                    })
            }
            composable(
                route = NavigationDestinations.USER_SCREEN,
            ) { backStackEntry ->

                UserScreen(viewModel)
            }
            composable(
                route = "${NavigationDestinations.POST_SCREEN}/{userId}",
                arguments = listOf(navArgument("userId") {
                    type = NavType.IntType
                })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId")

                if (userId != null) {
                    PostScreen(viewModel, userId, onPostSelected = {
                        navController.navigate(NavigationDestinations.POST_SCREEN)
                    })
                } else {
                    Text(
                        "Błąd: Nie można załadować postów użytkownika.",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

