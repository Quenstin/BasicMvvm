package com.hdyj.basicmodel.utils

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.blankj.utilcode.util.EncryptUtils


/**
 * Created by DanYue on 2021/12/25 16:27.
 */
object UniqueIdUtil {
    fun uuid(context: Context, isDef: Boolean = true): String {
        var sn = Build.SERIAL
        if (!isDef) {
            val androidId: String =
                Settings.System.getString(context.contentResolver, Settings.System.ANDROID_ID)
            val serialNumber = Build.SERIAL
            sn = EncryptUtils.encryptMD5ToString("${androidId}-${serialNumber}")
        }
        if (sn.isEmpty()) sn = "0123456789ABCDEF"
        hdyjLogd("uuid", "sn: $sn")
        return sn
    }
}