package com.biglist.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.biglist.data.AppRepository
import com.biglist.model.Todo
import com.biglist.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class UserViewModel(private val repository: AppRepository) : ViewModel() {

    private val _userWithTodosUiState =
        MutableStateFlow<UserWithTodosUiState>(UserWithTodosUiState.Loading)
    val userWithTodosUiState: StateFlow<UserWithTodosUiState> = _userWithTodosUiState.asStateFlow()


    fun getUserWithTodos(userId: Int) {
        _userWithTodosUiState.value = UserWithTodosUiState.Loading

        viewModelScope.launch {
            try {
                val user = repository.getUserById(userId)
                user.id?.let { id ->
                    val todos = repository.getTodoByUserId(userId)
                    _userWithTodosUiState.value = UserWithTodosUiState.Success(user, todos)
                }

            } catch (e: IOException) {
                _userWithTodosUiState.value =
                    UserWithTodosUiState.Error(e.localizedMessage ?: "Error fetching post and user")
            }
        }
    }


    sealed class UserWithTodosUiState {
        data object Loading : UserWithTodosUiState()
        data class Success(val user: User, val todos: List<Todo>?) : UserWithTodosUiState()
        data class Error(val error: String) : UserWithTodosUiState()
    }

}

@Suppress("UNCHECKED_CAST")
class UserViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Error")
    }
}