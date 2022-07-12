package com.hdyj.basicmodel.ext

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.alibaba.android.arouter.launcher.ARouter
import com.hdyj.basicmodel.widget.ToastCustom
import com.hdyj.basicmodel.view.fragment.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by hdyjzgq
 * data on 5/14/21
 * function is ：fragment拓展函数
 */

fun BaseFragment<*>.getResColor(color: Int)= ContextCompat.getColor(requireContext(),color)


fun BaseFragment<*>.jumpActivity(path: String,titleKey:String,titleValue: String) {
    ARouter.getInstance().build(path).withString(titleKey, titleValue)
        .navigation(requireActivity(), object : NavigationCallback {
            override fun onFound(postcard: Postcard?) {

            }

            override fun onLost(postcard: Postcard?) {
            }

            override fun onArrival(postcard: Postcard?) {
            }

            override fun onInterrupt(postcard: Postcard?) {
            }
        })

}


/**
 * 统一的自定义toast
 */
fun Fragment.toastShow(title:String = "提示", content: CharSequence){
    lifecycleScope.launch(Dispatchers.Main){
        ToastCustom.createToast(requireActivity(),title,content)
    }
}

