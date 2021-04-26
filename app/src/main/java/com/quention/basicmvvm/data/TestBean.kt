package com.quention.basicmvvm.data

import com.quenstin.basicmodel.data.BaseResponseEmpty

/**
 * Created by hdyjzgq
 * data on 4/26/21
 * function is ：
 */
data class TestBean(
    val guanggao: List<Guanggao>,
    val news: List<New>,
    val shiping: List<Shiping>,
    val status: Int = 0,
    val yuedu: List<Yuedu>


): BaseResponseEmpty() {
    override fun toString(): String {
        return "TestBean(guanggao=$guanggao, news=$news, shiping=$shiping, status=$status, yuedu=$yuedu)"
    }
}

data class Guanggao(
    val imageurl: String,
    val link: String,
    val title: String

) {
    override fun toString(): String {
        return "Guanggao(imageurl='$imageurl', link='$link', title='$title')"
    }
}

data class New(
    val thumb: String,
    val title: String
)

data class Shiping(
    val thumb: String,
    val title: String


) {
    override fun toString(): String {
        return "Shiping(thumb='$thumb', title='$title')"
    }
}

data class Yuedu(
    val description: String,
    val dizhi: String,
    val id: String,
    val name: String,
    val thumb: String,
    val title: String

) {
    override fun toString(): String {
        return "Yuedu(description='$description', dizhi='$dizhi', id='$id', name='$name', thumb='$thumb', title='$title')"
    }
}