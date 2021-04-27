package com.quention.basicmvvm.data

/**
 * Created by hdyjzgq
 * data on 4/26/21
 * function is ：
 */
data class TestBean(
    val errorCode: Int, val errorMsg: String, val data: ArrayList<DataBean>


) {

}

data class DataBean(
    var desc: String = "",
    var id: Int = 0,
    var imagePath: String = "",
    var isVisible: Int = 0,
    var order: Int = 0,
    var title: String = "",
    var type: Int = 0,
    var url: String = ""
) {}