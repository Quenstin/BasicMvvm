package com.quention.basicmvvm.repository

import com.quenstin.basicmodel.http.RetrofitFactory
import com.quenstin.basicmodel.repository.BaseRepository
import com.quention.basicmvvm.service.TestService

/**
 * Created by hdyjzgq
 * data on 4/26/21
 * function is ：
 */
abstract class ApiRepository :BaseRepository(){
    protected val apiService: TestService by lazy {
        RetrofitFactory.instance.create(TestService::class.java)

    }
}