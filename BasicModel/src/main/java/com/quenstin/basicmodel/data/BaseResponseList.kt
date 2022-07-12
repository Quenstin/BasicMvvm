package com.hdyj.basicmodel.data

/**
 * Created by hdyjzgq
 * data on 4/9/21
 * function is ：返回对象中有list的
 */
data class BaseResponseList<T>(
    val code: Int,
    val msg: String,
    val time: String,
    var data: List<T>
)