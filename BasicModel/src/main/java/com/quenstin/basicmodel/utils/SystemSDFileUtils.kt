package com.quenstin.basicmodel.utils

import android.content.Context
import android.os.storage.StorageManager
import java.io.File
import java.lang.reflect.Array

/**
 * Created by hdyjzgq
 * data on 2021/9/30
 * function is ：获取sd卡目录
 */
object SystemSDFileUtils {
    fun getStoragePath(mContext: Context): String {
        var targetpath = ""
        val mStorageManager = mContext.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        var storageVolumeClazz: Class<*>? = null
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume")
            val getVolumeList = mStorageManager.javaClass.getMethod("getVolumeList")
            val getPath = storageVolumeClazz.getMethod("getPath")
            val result = getVolumeList.invoke(mStorageManager)
            val length = Array.getLength(result)
            for (i in 0 until length) {
                val storageVolumeElement = Array.get(result, i)
                val path = getPath.invoke(storageVolumeElement) as String
                targetpath = path
                val dirFile = File(targetpath)
                if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
                    dirFile.mkdirs()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return targetpath
    }
}