package com.biglist.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.biglist.data.AppRepository
import com.biglist.model.Post
import com.biglist.model.User
import com.biglist.ui.viewModels.HomeViewModel.PostsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class PostViewModel(private val repository: AppRepository) : ViewModel() {

    private val _postWithUserUiState =
        MutableStateFlow<PostWithUserUiState>(PostWithUserUiState.Loading)
    val postWithUserUiState: StateFlow<PostWithUserUiState> = _postWithUserUiState.asStateFlow()

    fun getPostWithUser(postId: Int) {
        _postWithUserUiState.value = PostWithUserUiState.Loading

        viewModelScope.launch {
            try {
                val post = repository.getPostById(postId)
                post.userId?.let { userId ->
                    val user = repository.getUserById(userId)
                    _postWithUserUiState.value = PostWithUserUiState.Success(post, user)
                }

            } catch (e: IOException) {
                _postWithUserUiState.value =
                    PostWithUserUiState.Error(e.localizedMessage ?: "Error fetching post and user")
            }
        }
    }

    sealed class PostWithUserUiState {
        data object Loading : PostWithUserUiState()
        data class Success(val post: Post, val user: User?) : PostWithUserUiState()
        data class Error(val error: String) : PostWithUserUiState()
    }

    sealed class PostUiState {
        data object Loading : PostUiState()
        data class Success(val post: Post) : PostUiState()
        data class Error(val error: Throwable) : PostUiState()
    }

    sealed class UserUiState {
        data object Loading : UserUiState()
        data class Success(val user: User) : UserUiState()
        data class Error(val error: Throwable) : UserUiState()
    }

}

@Suppress("UNCHECKED_CAST")
class PostViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostViewModel::class.java)) {
            return PostViewModel(repository) as T
        }
        throw IllegalArgumentException("Error")
    }
}