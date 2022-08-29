package com.quenstin.basicmodel.utils.zip

import com.quenstin.basicmodel.utils.hdyjLogd
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.CRC32
import java.util.zip.CheckedOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream


/**
 * Created by DanYue on 2022/6/1 15:44.
 * Zip压缩
 */
object ZipUtil {

    /**
     * 压缩文件和文件夹
     *
     * @param srcFileString 要压缩的文件或文件夹
     * @param zipFileString 压缩完成的Zip路径
     * @throws Exception 异常
     */
    @Throws(java.lang.Exception::class)
    fun zipFolder(srcFileString: String?, zipFileString: String?) {
        //创建ZIP
        val outZip = ZipOutputStream(CheckedOutputStream(FileOutputStream(zipFileString), CRC32()))
        //创建文件
        val file = File(srcFileString)
        //压缩
        hdyjLogd("ZipFolder", "---->${file.parent}===${file.absolutePath}")
        zipFiles(outZip, file.name, file)
        //完成和关闭
        outZip.finish()
        outZip.close()
    }

    /**
     * 压缩文件
     *
     * @param folderString 文件夹路径
     * @param fileString 文件名
     * @param zipOutputSteam 压缩输出流
     * @throws Exception 异常
     */
    @Throws(IOException::class)
    private fun zipFiles(zipOutputStream: ZipOutputStream, name: String, fileSrc: File) {
        var fileName = name
        if (fileSrc.isDirectory) {
            hdyjLogd("zipFiles", "需要压缩的地址是目录")
            val files = fileSrc.listFiles()
            fileName = "$fileName/"
            zipOutputStream.putNextEntry(ZipEntry(fileName)) // 建一个文件夹
            hdyjLogd("zipFiles", "目录名: $fileName")
            for (f in files) {
                zipFiles(zipOutputStream, fileName + f.name, f)
                hdyjLogd("zipFiles", "目录: " + fileName + f.name)
            }
        } else {
            hdyjLogd("zipFiles", "需要压缩的地址是文件")
            zipOutputStream.putNextEntry(ZipEntry(fileName))
            hdyjLogd("zipFiles", "文件名: $fileName")
            val input = FileInputStream(fileSrc)
            hdyjLogd("zipFiles", "文件路径: $fileSrc")
            val buf = ByteArray(1024)
            var len: Int = -1
            while (input.read(buf).also { len = it } != -1) {
                zipOutputStream.write(buf, 0, len)
            }
            zipOutputStream.flush()
            input.close()
        }
    }
}