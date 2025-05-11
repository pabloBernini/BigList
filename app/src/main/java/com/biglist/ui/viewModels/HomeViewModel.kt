package com.biglist.ui.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.biglist.data.AppRepository
import com.biglist.model.Post
import com.biglist.model.User
import kotlinx.coroutines.launch
import java.io.IOException

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle // Import this
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: AppRepository) : ViewModel() {

    private val _usersUiState = MutableStateFlow<UsersUiState>(UsersUiState.Loading)
    val usersUiState: StateFlow<UsersUiState> = _usersUiState.asStateFlow()

    private val _postsUiState = MutableStateFlow<PostsUiState>(PostsUiState.Loading) // Use Initial state for posts initially
    val postsUiState: StateFlow<PostsUiState> = _postsUiState.asStateFlow()

    init {
        getAllUsersViewModel()
    }

    private fun getAllUsersViewModel() {
        _usersUiState.value = UsersUiState.Loading // Set loading state before fetching

        viewModelScope.launch {
            try {
                val fetchedUsers = repository.getAllUsers()
                _usersUiState.value = UsersUiState.Success(fetchedUsers) // Set success state with data
            } catch (e: IOException) {
                _usersUiState.value = UsersUiState.Error(e) // Set error state
            }
        }
    }

    fun onUserSelected(user: User) {
        _postsUiState.value = PostsUiState.Loading // Set loading state before fetching

        viewModelScope.launch {
            try {
                val fetchedPosts = repository.getUserPosts(user.id ?: 0)
                _postsUiState.value = PostsUiState.Success(fetchedPosts) // Set success state with data
            } catch (e: IOException) {
                _postsUiState.value = PostsUiState.Error(e) // Set error state
            }
        }
    }

    // Define sealed class for Users UI State
    sealed class UsersUiState {
        data object Loading : UsersUiState()
        data class Success(val users: List<User>) : UsersUiState()
        data class Error(val error: Throwable) : UsersUiState()
    }

    // Define sealed class for Posts UI State
    sealed class PostsUiState {
        data object Loading : PostsUiState()
        data class Success(val posts: List<Post>) : PostsUiState()
        data class Error(val error: Throwable) : PostsUiState()
    }
}

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Error")
    }
}