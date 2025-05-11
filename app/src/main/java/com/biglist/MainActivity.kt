package com.biglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.biglist.ui.screens.HomeScreen
import com.biglist.ui.viewModels.HomeViewModel
import com.biglist.ui.viewModels.HomeViewModelFactory
import com.biglist.ui.theme.ListUsersTheme



class MainActivity : ComponentActivity() {
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as BigListApplication
        val factory = HomeViewModelFactory(app.repository)
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        setContent {
            ListUsersTheme {
                HomeScreen(viewModel)
            }
        }
    }
}

