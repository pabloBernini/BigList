package com.biglist.data

import com.biglist.model.Post
import com.biglist.model.User


interface AppRepository {
    suspend fun getAllUsers(): List<User>
    suspend fun getAllPosts(): List<Post>

    suspend fun getUserPosts(userId: Int): List<Post>
    suspend fun getPostById(id: Int): Post
    suspend fun getUserById(id: Int): User

}

class AppRepositoryImpl(private val retrofitService: AppService): AppRepository {
    override suspend fun getAllUsers(): List<User> {
        return retrofitService.getAllUsers()
    }
    override suspend fun getAllPosts(): List<Post> {
        return retrofitService.getAllPosts()
    }

    override suspend fun getUserPosts(userId: Int): List<Post> {
        return retrofitService.getUserPosts(userId)
    }
    override suspend fun getPostById(id: Int): Post {
        return retrofitService.getPostById(id)
    }
    override suspend fun getUserById(id: Int): User {
        return retrofitService.getUserById(id)
    }
}