package com.quenstin.basicmodel.utils

import android.content.Context
import com.quenstin.basicmodel.ext.showDialog
import com.quenstin.basicmodel.widget.dialog.HProgressDialog


/**
 * Created by hdyjzgq
 * data on 2022/4/1
 * function is ：带横向进度条的dialog
 */
object HProgressDialogUtils {

    private var sHorizontalProgressDialog: HProgressDialog? = null


    fun showHorizontalProgressDialog(
        context: Context,
        title: String = "检查更新",
        tip: String = "正在更新中..."
    ) {
        if (sHorizontalProgressDialog == null) {
            sHorizontalProgressDialog = HProgressDialog(context)
        }
        sHorizontalProgressDialog?.let {
            it.showDialog()
            it.setTitleAndHint(title, tip)
        }
    }


    fun cancel() {
        if (sHorizontalProgressDialog != null) {
            sHorizontalProgressDialog!!.dismiss()
            sHorizontalProgressDialog = null
        }
    }

    fun setProgress(current: Int) {
        if (sHorizontalProgressDialog == null) {
            return
        }
        sHorizontalProgressDialog!!.setHProgressDialogProgress(current)
        if (current >= 100) {
            sHorizontalProgressDialog!!.dismiss()
            sHorizontalProgressDialog = null
        }
    }


}