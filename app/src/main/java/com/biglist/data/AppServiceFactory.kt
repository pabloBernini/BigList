package com.biglist.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppServiceFactory{
    fun makeRetrofitService() : AppService {
        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(AppService::class.java)

    }
}