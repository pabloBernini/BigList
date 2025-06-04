package com.biglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
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
import com.biglist.ui.screens.UserDetailsScreen
import com.biglist.ui.screens.UserScreen
import com.biglist.ui.viewModels.PostViewModel
import com.biglist.ui.viewModels.PostViewModelFactory
import com.biglist.ui.viewModels.UserDetailsViewModel
import com.biglist.ui.viewModels.UserViewModel
import com.biglist.ui.viewModels.UserViewModelFactory


class MainActivity : ComponentActivity() {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var postViewModel: PostViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var userDetailsViewModel: UserDetailsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as BigListApplication
        val homeFactory = HomeViewModelFactory(app.repository)
        val postFactory = PostViewModelFactory(app.repository)
        val userFactory = UserViewModelFactory(app.repository)

        userDetailsViewModel = ViewModelProvider(this)[UserDetailsViewModel::class.java]
        homeViewModel = ViewModelProvider(this, homeFactory)[HomeViewModel::class.java]
        postViewModel = ViewModelProvider(this, postFactory)[PostViewModel::class.java]
        userViewModel = ViewModelProvider(this, userFactory)[UserViewModel::class.java]


        setContent {
            ListUsersTheme {
                AppScreen(
                    homeViewModel = homeViewModel,
                    postViewModel = postViewModel,
                    userViewModel = userViewModel,
                    userDetailsViewModel = userDetailsViewModel
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen(
    homeViewModel: HomeViewModel,
    postViewModel: PostViewModel,
    userViewModel: UserViewModel,
    userDetailsViewModel: UserDetailsViewModel
) {
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
                            "${NavigationDestinations.USER_SCREEN}/{id}" -> "User Details"
                            "${NavigationDestinations.POST_SCREEN}/{id}" -> "Post Details"
                            NavigationDestinations.USER_DETAILS_SCREEN -> "My Details"
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
                        IconButton(onClick = { navController.navigate(NavigationDestinations.USER_DETAILS_SCREEN) }) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = "Back",
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.secondary)
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavigationDestinations.HOME_SCREEN,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(
                NavigationDestinations.HOME_SCREEN
            ) {
                HomeScreen(
                    viewModel = homeViewModel,
                    onUserSelected = { user ->
                        navController.navigate("${NavigationDestinations.USER_SCREEN}/${user.id}")
                    },
                    onPostSelected = { post ->
                        navController.navigate("${NavigationDestinations.POST_SCREEN}/${post.id}")
                    }
                )
            }
            composable(
                route = "${NavigationDestinations.USER_SCREEN}/{id}",
                arguments = listOf(navArgument("id") {
                    type = NavType.IntType
                })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("id")

                if (userId != null) {
                    UserScreen(userViewModel, userId)
                } else {
                    Text(
                        "Error, can't load user details",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            composable(
                route = "${NavigationDestinations.POST_SCREEN}/{id}",
                arguments = listOf(navArgument("id") {
                    type = NavType.IntType
                })
            ) { backStackEntry ->
                val postId = backStackEntry.arguments?.getInt("id")

                if (postId != null) {
                    PostScreen(postViewModel, postId)
                } else {
                    Text(
                        "Error, can't load post details",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            composable(route = NavigationDestinations.USER_DETAILS_SCREEN) {
                UserDetailsScreen(
                    viewModel = userDetailsViewModel,
                onUserDetailsSaved = {
                    navController.navigate(NavigationDestinations.HOME_SCREEN) {
                        popUpTo(NavigationDestinations.HOME_SCREEN) {
                            inclusive = true
                        }
                }}
                )
            }
        }
    }
}
