package com.biglist.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
fun PostScreen(viewModel: HomeViewModel, userId: Int, onPostSelected: () -> Unit) {

/////////////
    val postsUiState by viewModel.postsUiState.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier.padding(16.dp).clickable{onPostSelected()}
    ){

    Text(text = "$userId")
    }
}
/////////////////////


@Composable
fun PostItemScreen(post: Post) {
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