package com.biglist.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.biglist.ui.viewModels.HomeViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel : HomeViewModel) {
    var currentScreen by remember {mutableStateOf("userScreen")}

    var topBarTitle by remember {mutableStateOf("Users List")}

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(topBarTitle, color = Color.White)
                    }
                },
                navigationIcon = {
                    if(currentScreen == "postScreen") {
                        IconButton(onClick = {
                            currentScreen = "userScreen"
                            topBarTitle = "Users List"
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Return",
                                tint = Color.White,
                            )
                        }
                    }else{
                        Icon (
                            imageVector = Icons.Default.Home,
                            contentDescription = "User",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        }
    ){paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            when (currentScreen){
                "userScreen" -> {
                    HomeList(viewModel){user ->
                        ///// jak zabraknie tej funkcji,
                        ////to ne laduja sie posty,
                        ///czyli trzeba dodac to samo w
                        ////viewmodelu i ew przekazac dali
                        viewModel.onUserSelected(user)
                        topBarTitle = "${user.username} Posts"
                        currentScreen = "postScreen"
                        Log.d("aaa", "bbbb")
                    }
                }
                "postScreen" -> {
                    PostScreen(viewModel)
                }
            }
        }
    }
}