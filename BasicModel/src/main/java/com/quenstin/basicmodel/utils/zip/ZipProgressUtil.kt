package com.quenstin.basicmodel.utils.zip

import com.quenstin.basicmodel.utils.zip.UnZipMainThread
import com.quenstin.basicmodel.utils.zip.ZipListener

/**
 * Created by hdyjzgq
 * data on 2021/8/31
 * function is ：带进度的解压
 */
object ZipProgressUtil {
    /***
     * 解压通用方法
     *
     * @param zipFileString
     * 文件路径
     * @param outPathString
     * 解压路径
     * @param listener
     * 加压监听
     */
    fun unZipFile(zipFileString: String, outPathString: String, listener: ZipListener) {
        val zipThread: Thread = UnZipMainThread(zipFileString, outPathString, listener)
        zipThread.start()
    }
}