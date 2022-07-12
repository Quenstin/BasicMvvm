package com.hdyj.basicmodel.widget.dialog

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.SPUtils
import com.example.basicmodel.R
import com.hdyj.basicmodel.common.BasicCommon
import com.hdyj.basicmodel.utils.NavigationBarUtils

/**
 *
 * @Description:
 * @Author:         jyb
 * @CreateDate:     2022/5/24
 */
class EyeDialog(mContext: Context, eyeTimeType: Int) {
    private var mBottomDialog: Dialog? = null
    private var window: Window? = null

    init {

        mBottomDialog = Dialog(mContext, R.style.bottom_dialog)
        mBottomDialog!!.setContentView(R.layout.eye_time_dialog_layout)
        window = mBottomDialog!!.window
        window!!.setBackgroundDrawableResource(R.color.transparent);
        //8.0系统加强后台管理，禁止在其他应用和窗口弹提醒弹窗，如果要弹，必须使用TYPE_APPLICATION_OVERLAY
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window!!.setType((WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY));
        } else {
            window!!.setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        }

        window!!.setGravity(Gravity.TOP) //可设置dialog的位置
        window!!.decorView.setPadding(0, 0, 0, 0) //消除边距
        val d = window!!.windowManager.defaultDisplay // 获取屏幕宽、高用
        val lp = window!!.attributes // 获取对话框当前的参数值
        lp.height = (d.height * 0.08).toInt() // 高度设置为屏幕的0.12
        lp.width = d.width
        window!!.attributes = lp

        var contentName = mBottomDialog!!.findViewById<TextView>(R.id.eyeContentTV)

        when (eyeTimeType) {
            0 ->{
                contentName.text = "学习5分钟了，让眼睛休息会吧。"
            }
            1 -> {
                contentName.text = "学习半小时了，让眼睛休息会吧。"
            }
            2 -> {
                contentName.text = "学习1小时了，让眼睛休息会吧。"
            }
            3 -> {
                contentName.text = "学习2小时了，让眼睛休息会吧。"
            }
            4 -> {
                contentName.text = "学习3小时了，让眼睛休息会吧。"
            }
            5 -> {
                contentName.text = "学习4小时了，让眼睛休息会吧。"
            }
        }

        /**
         * 点击设置  跳转到护眼模式
         */
        mBottomDialog!!.findViewById<TextView>(R.id.eye_settingTV).setOnClickListener {
            ARouter.getInstance().build("/settingmodule/EyeProtectionActivity").navigation()
            dismissDialog()
        }

        /**
         * 点击 外部 弹框消失
         */
        mBottomDialog!!.setOnDismissListener {
            SPUtils.getInstance().put(BasicCommon.appUseTime, 0L)
        }

    }


    fun showDialog(): Dialog {
        NavigationBarUtils.focusNotAle(window!!)
        mBottomDialog!!.show()
        NavigationBarUtils.hideNavigationBar(window!!)
        NavigationBarUtils.clearFocusNotAle(window!!)
        return mBottomDialog!!
    }

     fun dismissDialog() {
        mBottomDialog!!.dismiss()
    }

}