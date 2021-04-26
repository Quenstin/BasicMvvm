package com.quention.basicmvvm.service

import com.quenstin.basicmodel.service.homeUrl
import com.quention.basicmvvm.data.TestBean
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by hdyjzgq
 * data on 4/26/21
 * function is ：
 */
interface TestService {
    @FormUrlEncoded
    @POST(homeUrl)
    suspend fun homeApi(@Field("nianji") nianji: String): TestBean
}