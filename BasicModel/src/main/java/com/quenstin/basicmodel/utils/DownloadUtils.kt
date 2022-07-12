package com.hdyj.basicmodel.utils

import android.app.*
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import cn.dreamtobe.filedownloader.OkHttp3Connection
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.Utils
import com.hdyj.basicmodel.BaseApplication
import com.hdyj.basicmodel.utils.zip.ZipListener
import com.hdyj.basicmodel.utils.zip.ZipProgressUtil
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import com.liulishuo.filedownloader.database.RemitDatabase
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Created by hdyjzgq
 * data on 2021/10/11
 * function is ：下载使用的工具类
 */
class DownloadUtils : FileDownloadListener() {

    /**
     * 下载文件保存的目录
     */
    private var mSavePath: String? = null

    /**
     * 解压目录
     */
    private var mDirPath: String? = null

    private var mZipFileName: String? = null


    private var mActivity: Activity? = null

    private var mFileNotFoundHandle: (() -> Unit)? = null

    private var mZipSuccessListener: (() -> Unit)? = null

    fun setActivity(activity: Activity): DownloadUtils {
        mActivity = activity
        return this
    }

    fun setPath(savePath: String, unZipPath: String): DownloadUtils {
        mSavePath = savePath
        mDirPath = unZipPath
        return this
    }

    fun setPath(savePath: String, unZipPath: String, zipFilename: String): DownloadUtils {
        mSavePath = savePath
        mDirPath = unZipPath
        mZipFileName = zipFilename
        return this
    }

    /**
     * 设置无效下载链接时处理方法，不设置则直接提示下载失败
     *
     * @param block 404时处理方法
     */
    fun setFileNotFoundHandle(block: () -> Unit): DownloadUtils {
        mFileNotFoundHandle = block
        return this
    }

    /**
     * 解压完成通知
     */
    fun setZipSuccessListener(block: () -> Unit): DownloadUtils {
        mZipSuccessListener = block
        return this
    }

    /**
     * 初始化下载器
     * FileDownloadUrlConnection.Creator(
    FileDownloadUrlConnection.Configuration()
    .connectTimeout(15000) // set connection timeout.
    .readTimeout(15000) // set read timeout.
    )
     */
    fun initDownload(application: Application) {
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.connectTimeout(15000, TimeUnit.SECONDS)
        FileDownloader.setupOnApplicationOnCreate(application)
            .connectionCreator(
                OkHttp3Connection.Creator(okHttpClient)
            )
            .database(RemitDatabase.Maker())
            .commit()

    }

    /**
     * 开启下载服务
     */
    fun startService() {
        AppUtils.registerAppStatusChangedListener(object : Utils.OnAppStatusChangedListener {


            override fun onForeground(activity: Activity?) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FileDownloader.getImpl().stopForeground(true)
                }
            }

            override fun onBackground(activity: Activity?) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channelId = createNotificationChannel("downloadService", "在听学盒子中下载")
                    val notificationBuilder =
                        NotificationCompat.Builder(BaseApplication.mInstance, channelId).build()
                    FileDownloader.getImpl().startForeground(101, notificationBuilder)
                }
            }
        })
        FileDownloader.getImpl().bindService {
            FileDownloader.enableAvoidDropFrame()
        }
    }

    /**
     * 开启下载
     * url：下载链接
     * savePath:保存地址
     * listener：下载监听
     */
    fun startDownload(url: String): Int {
        return FileDownloader.getImpl().create(url)
            .setPath(mSavePath, false)
            .setCallbackProgressTimes(300)
            .setMinIntervalUpdateSpeed(400)
            .setListener(this).start()

    }

    /**
     * 获取下载状态
     * completed = -3;
     * warn = -4;
     * paused = -2;
     * error = -1;
     */
    fun getDownloadStatus(downloadUrl: String, savePath: String): Byte {
        return FileDownloader.getImpl().getStatus(downloadUrl, savePath)
    }

    fun dismissDialog() {
        HProgressDialogUtils.cancel()
        mActivity = null
    }

    /**
     * 暂停所有下载
     */
    fun pauseAll() {
        FileDownloader.getImpl().pauseAll()
    }

    /**
     * 暂停单个下载
     */
    fun pauseSingle(singleTaskId: Int) {
        FileDownloader.getImpl().pause(singleTaskId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service =
            BaseApplication.mInstance.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    override fun pending(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
        HProgressDialogUtils.showHorizontalProgressDialog(mActivity!!)
        hdyjLoge("pending")
    }

    override fun progress(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
        HProgressDialogUtils.setProgress((soFarBytes * 1.0f / totalBytes * 100).toInt())
    }

    override fun completed(task: BaseDownloadTask?) {
        HProgressDialogUtils.cancel()
        val zipPath =
            if (mZipFileName.isNullOrEmpty()) mSavePath else "${mSavePath!!}/$mZipFileName.zip"
        ZipProgressUtil.unZipFile(
            zipPath!!,
            mDirPath!!,
            object : ZipListener {
                override fun zipStart() {
                    HProgressDialogUtils.showHorizontalProgressDialog(
                        mActivity!!,
                        "解压资源",
                        "解压中，请耐心等待..."
                    )
                }

                override fun zipSuccess() {
                    FileUtils.delete(zipPath)
                    HProgressDialogUtils.cancel()
                    mZipSuccessListener?.invoke()

                }

                override fun zipProgress(progress: Int) {
                    HProgressDialogUtils.setProgress(progress)
                    hdyjLogd("解压中$progress")
                }

                override fun zipFail() {
                    HProgressDialogUtils.cancel()
                }

            })


    }

    override fun paused(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
        HProgressDialogUtils.cancel()
    }

    override fun error(task: BaseDownloadTask?, e: Throwable?) {
        e?.message?.let {
            //返回404 并且 有处理预案
            if (it.contains("response code error: 404") && mFileNotFoundHandle != null) {
                mFileNotFoundHandle!!()
            } else {
                hdyjLoge("下载失败",it)
                toastShow(mActivity!!, content = "下载失败")
            }
        }
        HProgressDialogUtils.cancel()
    }

    override fun warn(task: BaseDownloadTask?) {
        hdyjLoge("下载警告---")
        HProgressDialogUtils.cancel()
    }
}

