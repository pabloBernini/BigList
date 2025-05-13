package com.biglist.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.biglist.model.Todo
import com.biglist.model.User
import com.biglist.ui.viewModels.UserViewModel

@Composable
fun UserScreen(viewModel: UserViewModel, userId: Int) {

/////////////
    LaunchedEffect(userId) {
        viewModel.getUserWithTodos(userId)

    }
    val userWithTodosUiState by viewModel.userWithTodosUiState.collectAsStateWithLifecycle()

    when (userWithTodosUiState) {
        is UserViewModel.UserWithTodosUiState.Loading -> {
            LoadingIndicator()
        }

        is UserViewModel.UserWithTodosUiState.Success -> {
            val user = (userWithTodosUiState as UserViewModel.UserWithTodosUiState.Success).user
            val todos = (userWithTodosUiState as UserViewModel.UserWithTodosUiState.Success).todos
            if (todos != null) {

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    UserScreenContent(user = user)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(todos) { todo ->
                            TodoItem(todo = todo)
                        }
                    }
                }
            }
        }
        is UserViewModel.UserWithTodosUiState.Error -> {
            val error = (userWithTodosUiState as UserViewModel.UserWithTodosUiState.Error).error
            ErrorMessage(error)
        }
    }
}

/////////////////////


@Composable
fun UserScreenContent(user: User) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = user.username ?: "",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = user.email ?: "No email available",
            fontSize = 16.sp,
            color = Color.Gray
        )


    }
}

@Composable
fun TodoItem(todo: Todo) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = todo.title ?: "",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Is todo done? ${todo.completed}",
            fontSize = 16.sp,
            color = Color.Gray
        )


    }
}