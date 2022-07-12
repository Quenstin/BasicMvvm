package com.hdyj.basicmodel.utils.zip

import com.hdyj.basicmodel.utils.zip.ZipListener
import com.hdyj.basicmodel.utils.zip.UnZipMainThread

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