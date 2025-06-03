package com.biglist.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.biglist.model.Todo
import com.biglist.model.User
import com.biglist.ui.viewModels.UserViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun UserScreen(viewModel: UserViewModel, userId: Int) {
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
            val lat = user.address?.geo?.lat?.toDoubleOrNull() ?: 0.0
            val lng = user.address?.geo?.lng?.toDoubleOrNull() ?: 0.0
            val cameraPosition = LatLng(lat, lng)
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    UserScreenContent(user = user)
                }



                todos?.let { todoList ->
                    items(todoList) { todo ->
                        TodoItem(todo = todo)
                    }
                }
                item {


                    ///////////////////////////////google map

                    val markerState = remember { MarkerState(position = cameraPosition) }
                    Column {
                        Text(
                            text = "User Location",
                            modifier = Modifier.padding(start = 24.dp, top = 16.dp, bottom = 4.dp),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Card(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .fillMaxWidth()
                                .height(220.dp),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ) {
                            GoogleMap(
                                modifier = Modifier.fillMaxSize(),
                                cameraPositionState = rememberCameraPositionState {
                                    position = CameraPosition.fromLatLngZoom(cameraPosition, 1f)
                                }
                            ) {
                                Marker(
                                    state = markerState,
                                    title = user.name,
                                    snippet = user.address?.city
                                )
                            }
                        }
                    }
                    ///////////////////////
            }}
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
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Name - ${user.name}", fontSize = 15.sp, fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Username - ${user.username}", fontSize = 15.sp, fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Email - ${user.email}", fontSize = 15.sp, fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Phone Number - ${user.phone}", fontSize = 15.sp, fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Website - ${user.website}", fontSize = 15.sp, fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Company:", fontSize = 15.sp, fontWeight = FontWeight.Bold
        )
        Text(
            text = "Street - ${user.company?.name}\n" + "Suite - ${user.company?.catchPhrase}\n" + "City - ${user.company?.bs}",
            fontSize = 15.sp,
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Address:", fontSize = 15.sp, fontWeight = FontWeight.Bold
        )
        Text(
            text = "Street - ${user.address?.street}\n" + "Suite - ${user.address?.suite}\n" + "City - ${user.address?.city}\n" + "Zipcode - ${user.address?.zipcode}",
            fontSize = 15.sp,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Todos",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TodoItem(todo: Todo) {
    Row(
        modifier = Modifier.padding(16.dp, 0.dp)
    ) {
        Text(
            text = todo.title ?: "", fontSize = 15.sp
        )
        Spacer(modifier = Modifier.width(10.dp))
        Checkbox(checked = todo.completed ?: false, onCheckedChange = null, enabled = false)


    }
}