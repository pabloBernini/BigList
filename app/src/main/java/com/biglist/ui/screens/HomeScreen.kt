package com.biglist.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.biglist.model.User
import com.biglist.ui.viewModels.HomeViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue
import com.biglist.model.Post

@Composable
fun HomeScreen(viewModel: HomeViewModel, onUserSelected: (User) -> Unit) {
    val postsUiState by viewModel.testUiState.collectAsStateWithLifecycle()

    when (postsUiState) {
        is HomeViewModel.PostsUiState.Loading -> {
            LoadingIndicator()
        }

        is HomeViewModel.PostsUiState.Success -> {
            val postsList = (postsUiState as HomeViewModel.PostsUiState.Success).posts

            LazyColumn {
                items(postsList) { post ->
                    val user = viewModel.getUserById(post.userId ?: 0)
                    if (user != null)
                        PostItemHome(post = post, user = user, onUserSelected)
                }
            }
        }

        is HomeViewModel.PostsUiState.Error -> {
            val error = (postsUiState as HomeViewModel.PostsUiState.Error).error
            ErrorMessage(error.localizedMessage ?: "An error occurred")
        }
    }
}


@Composable
fun LoadingIndicator() {
    Text("Loading...")
}

@Composable
fun ErrorMessage(message: String) {
    Text("Error: $message")
}

@Composable
fun UserItemScreen(user: User, onUserClick: (User) -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White)
            .clickable { onUserClick(user) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "User",
                tint = Color.Black,
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = user.username ?: "UNKNOWN",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = user.name ?: "UNKNOWN",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun PostItemHome(post: Post, user: User, onUserSelected: (User) -> Unit) {

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        UserItemScreen(user = user, onUserClick = { user ->
            onUserSelected(user)
        })
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