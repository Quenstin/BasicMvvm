package com.quention.basicmvvm.repository

import com.quention.basicmvvm.data.TestBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by hdyjzgq
 * data on 4/26/21
 * function is ：
 */
class TestRepository:ApiRepository() {
    suspend fun getHome(str: String): TestBean {
        return withContext(Dispatchers.IO) {
            apiService.homeApi(str)
        }
    }
}