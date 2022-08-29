package com.quenstin.basicmodel.ext

/**
 * Created by hdyjzgq
 * data on 2022/7/7
 * function is ：
 */

fun String.splitIsNotEmpty(content: String): List<String> {
    return this.split(content).filter { it.isNotEmpty() }
}