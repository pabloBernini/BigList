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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.biglist.model.Post
import com.biglist.model.User
import com.biglist.ui.viewModels.HomeViewModel
import com.biglist.ui.viewModels.PostViewModel

@Composable
fun PostScreen(viewModel: PostViewModel, postId: Int) {

    LaunchedEffect(postId) {
        viewModel.getPostWithUser(postId)

    }
    val postWithUserUiState by viewModel.postWithUserUiState.collectAsStateWithLifecycle()

    when (postWithUserUiState) {
        is PostViewModel.PostWithUserUiState.Loading -> {
            LoadingIndicator()
        }

        is PostViewModel.PostWithUserUiState.Success -> {
            val post = (postWithUserUiState as PostViewModel.PostWithUserUiState.Success).post
            val user = (postWithUserUiState as PostViewModel.PostWithUserUiState.Success).user
            if (user != null) {
            PostItemScreen(post, user)
            }}

        is PostViewModel.PostWithUserUiState.Error -> {
            val error = (postWithUserUiState as PostViewModel.PostWithUserUiState.Error).error
            ErrorMessage(error)
        }
    }}
/////////////
/////////////////////


@Composable
fun PostItemScreen(post: Post, user: User) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        UserItem(user)
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
@Composable
fun UserItem(user: User){
    Card(
        shape = RoundedCornerShape(1.dp),
        elevation  = CardDefaults.elevatedCardElevation(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.AccountCircle,
                contentDescription = "User",
                tint = Color.Black,
                modifier = Modifier.size(50.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = user.username ?:"UNKNOWN",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = user.name ?: "UNKNOWN",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "User id: ${user.id}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold)
        }
    }
}