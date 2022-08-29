package com.quenstin.basicmodel.utils.zip

import android.os.Looper
import com.quenstin.basicmodel.utils.hdyjLoge
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream

/**
 * Created by hdyjzgq
 * data on 2021/8/31
 * function is ：解压处理
 */
class UnZipMainThread(
    var zipFileString: String,
    var outPathString: String,
    var listener: ZipListener
) : Thread() {
    override fun run() {
        Looper.prepare()
        try {
            listener.zipStart()
            var sumLength: Long = 0
            // 获取解压之后文件的大小,用来计算解压的进度
            val ziplength = getZipTrueSize(zipFileString)
            println("====文件的大小==$ziplength")
            val inZip = ZipInputStream(
                FileInputStream(
                    zipFileString
                )
            )
            var zipEntry: ZipEntry
            var videoName = ""
            while (inZip.nextEntry.also { zipEntry = it } != null) {
                if (zipEntry.name.startsWith("__MACOSX")){
                    continue
                }
                videoName = zipEntry.name
                if (zipEntry.isDirectory) {
                    //获取部件的文件夹名
                    val folder = File(outPathString + File.separator + videoName)
                    folder.mkdirs()
                } else {
                    hdyjLoge(outPathString + File.separator + videoName)
                    val file = File(outPathString + File.separator + videoName)
                    if (!file.exists()) {
                        hdyjLoge("Create the file:" + outPathString + File.separator + videoName)
                        file.parentFile.mkdirs()
                        file.createNewFile()
                    }
                    // 获取文件的输出流
                    val out = FileOutputStream(file)
                    var len: Int
                    val buffer = ByteArray(1024)
                    // 读取（字节）字节到缓冲区
                    while (inZip.read(buffer).also { len = it } != -1) {
                        sumLength += len.toLong()
                        val progress = (sumLength * 100 / ziplength).toInt()
                        updateProgress(progress, listener)
                        // 从缓冲区（0）位置写入（字节）字节
                        out.write(buffer, 0, len)
                        out.flush()
                    }
                    out.close()
                }
            }
            listener.zipSuccess()
            inZip.close()
        } catch (e: Exception) {
            listener.zipFail()
        }
        Looper.loop()
    }

    var lastProgress = 0
    private fun updateProgress(progress: Int, listener2: ZipListener) {
        /** 因为会频繁的刷新,这里我只是进度>1%的时候才去显示  */
        if (progress > lastProgress) {
            lastProgress = progress
            listener2.zipProgress(progress)
        }
    }

    /**
     * 获取压缩包解压后的内存大小
     *
     * @param filePath 文件路径
     * @return 返回内存long类型的值
     */
    private fun getZipTrueSize(filePath: String?): Long {
        var size: Long = 0
        val f: ZipFile
        try {
            f = ZipFile(filePath)
            val en = f.entries()
            while (en.hasMoreElements()) {
                size += en.nextElement().size
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return size
    }

    companion object {
        private const val TAG = "UnZipMainThread"
    }
}