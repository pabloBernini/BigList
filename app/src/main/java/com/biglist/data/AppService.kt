package com.biglist.data

import com.biglist.model.Post
import com.biglist.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


    interface AppService {
        @GET("users")
        suspend fun getAllUsers(): List<User>

        @GET("posts")
        suspend fun getAllPosts(): List<Post>


        @GET("posts")
        suspend fun getUserPosts(@Query("userId") userId: Int): List<Post>

        @GET("/posts/{id}")
        suspend fun getPostById(@Path("id") id: Int): Post

        @GET("/users/{id}")
        suspend fun getUserById(@Path("id") id: Int): User
    }

