package com.hdyj.basicmodel.data

/**
 * Created by hdyjzgq
 * data on 4/9/21
 * function is ：返回是对象的
 */
data class BaseResponse<T>(
    val code: Int,
    val data: T,
    val msg: String,
    val time: String
)