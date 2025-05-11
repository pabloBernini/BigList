package com.biglist.ui.viewModels
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.biglist.data.AppRepository
import com.biglist.model.Post
import com.biglist.model.User
import kotlinx.coroutines.launch
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel(private val repository: AppRepository) : ViewModel() {

    private val _usersUiState = MutableStateFlow<UsersUiState>(UsersUiState.Loading)
    val usersUiState: StateFlow<UsersUiState> = _usersUiState.asStateFlow()

    private val _postsUiState = MutableStateFlow<PostsUiState>(PostsUiState.Loading)
    val postsUiState: StateFlow<PostsUiState> = _postsUiState.asStateFlow()

    private val _testUiState = MutableStateFlow<PostsUiState>(PostsUiState.Loading)
    val testUiState: StateFlow<PostsUiState> = _testUiState.asStateFlow()

    init {

        getAllPostsViewModel()
        getAllUsersViewModel()
    }

    private fun getAllUsersViewModel() {
        _usersUiState.value = UsersUiState.Loading

        viewModelScope.launch {
            try {
                val fetchedUsers = repository.getAllUsers()
                _usersUiState.value = UsersUiState.Success(fetchedUsers)

            } catch (e: IOException) {
                _usersUiState.value = UsersUiState.Error(e)
            }
        }
    }

    private fun getAllPostsViewModel() {
        _testUiState.value = PostsUiState.Loading

        viewModelScope.launch {
            try {
                val fetchedPosts = repository.getAllPosts()
                _testUiState.value = PostsUiState.Success(fetchedPosts)
                Log.d("aaa", "bbbb")

            } catch (e: IOException) {
                _testUiState.value = PostsUiState.Error(e)
            }
        }
    }


    fun onUserSelected(user: User) {
        _postsUiState.value = PostsUiState.Loading
        viewModelScope.launch {
            try {
                val fetchedPosts = repository.getUserPosts(user.id ?: 0)
                _postsUiState.value = PostsUiState.Success(fetchedPosts)
            } catch (e: IOException) {
                _postsUiState.value = PostsUiState.Error(e)
            }
        }
    }

    sealed class UsersUiState {
        data object Loading : UsersUiState()
        data class Success(val users: List<User>) : UsersUiState()
        data class Error(val error: Throwable) : UsersUiState()
    }

    sealed class PostsUiState {
        data object Loading : PostsUiState()
        data class Success(val posts: List<Post>) : PostsUiState()
        data class Error(val error: Throwable) : PostsUiState()
    }

    fun getUserById(id: Int): User? {
        val currentState = usersUiState.value
        if (currentState is UsersUiState.Success) {
            val usersList = currentState.users
            for (user in usersList) {
                if (user.id == id) {
                    return user
                }
            }
        }
        return null
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