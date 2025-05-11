package com.biglist

import android.app.Application
import com.biglist.data.AppRepository
import com.biglist.data.AppRepositoryImpl
import com.biglist.data.AppServiceFactory


class BigListApplication : Application(){
    lateinit var repository: AppRepository

    override fun onCreate(){
        super.onCreate()

        val retrofitService = AppServiceFactory.makeRetrofitService()
        repository = AppRepositoryImpl(retrofitService)
    }
}