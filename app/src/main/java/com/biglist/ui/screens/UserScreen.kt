package com.biglist.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.biglist.model.Post
import com.biglist.ui.viewModels.HomeViewModel

@Composable
fun UserScreen(viewModel: HomeViewModel) {

/////////////
    val postsUiState by viewModel.postsUiState.collectAsStateWithLifecycle()

    when (postsUiState) {
        is HomeViewModel.PostsUiState.Loading -> {
            LoadingIndicator()
            Log.d("PostScreen", "PostsUiState.Loading")

        }

        is HomeViewModel.PostsUiState.Success -> {
            Log.d("PostScreen", "PostsUiState.Success")
            val postsList = (postsUiState as HomeViewModel.PostsUiState.Success).posts
            LazyColumn {
                items(postsList) { post ->
                    UserItemScreen(post = post)
                }
            }
        }

        is HomeViewModel.PostsUiState.Error -> {
            val error = (postsUiState as HomeViewModel.PostsUiState.Error).error
            ErrorMessage(error.localizedMessage ?: "An error occurred")
        }
    }
}

/////////////////////


@Composable
fun UserItemScreen(post: Post) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = post.title ?: "",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = post.body ?: "No body available",
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}