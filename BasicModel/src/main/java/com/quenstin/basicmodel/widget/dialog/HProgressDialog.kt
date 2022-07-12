package com.hdyj.basicmodel.widget.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import com.example.basicmodel.R
import com.example.basicmodel.databinding.CommonHProgressDialogBinding
import com.hdyj.basicmodel.ext.setGravityStyle
import com.hdyj.basicmodel.ext.setShapeBg

/**
 * Created by hdyjzgq
 * data on 2022/5/10
 * function is ：带横向更新进度的dialog
 */
class HProgressDialog(context: Context) : Dialog(context, R.style.CommitDialog) {

    private lateinit var mViewBinding: CommonHProgressDialogBinding

    var onProgress: ((Int) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = CommonHProgressDialogBinding.inflate(layoutInflater)
        setContentView(mViewBinding.root)
        setGravityStyle(Gravity.CENTER)
        mViewBinding.commonHPRoot.setShapeBg(radius = 8)
        setCancelable(false)
    }

    /**
     * 设置进度条进度
     */
    fun setHProgressDialogProgress(progress: Int) {
        onProgress?.invoke(progress)
        mViewBinding.commonHProgress.progress = progress
        mViewBinding.commonHPNumber.text = "$progress%"
    }

    fun setTitleAndHint(title: String, tip: String) {
        mViewBinding.commonHPTitle.text = title
        mViewBinding.commonHPTip.text = tip
    }

    /**
     * 关闭dialog
     */
    fun cancelDialog() {
        if (isShowing) {
            dismiss()
        }
    }
}