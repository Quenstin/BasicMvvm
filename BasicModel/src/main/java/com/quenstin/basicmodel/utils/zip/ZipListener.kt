package com.hdyj.basicmodel.utils.zip

/**
 * Created by hdyjzgq
 * data on 2021/8/31
 * function is ：解压监听
 */
interface ZipListener {
    /** 开始解压  */
    fun zipStart()

    /** 解压成功  */
    fun zipSuccess()

    /** 解压进度  */
    fun zipProgress(progress: Int)

    /** 解压失败  */
    fun zipFail()
}