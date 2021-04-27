package com.quention.basicmvvm.service

import com.quention.basicmvvm.data.TestBean
import retrofit2.http.GET

/**
 * Created by hdyjzgq
 * data on 4/26/21
 * function is ：
 */
interface TestService {


    /**
     * 获取banner数据
     */
    @GET("banner/json")
    suspend fun getBanner(): TestBean
}