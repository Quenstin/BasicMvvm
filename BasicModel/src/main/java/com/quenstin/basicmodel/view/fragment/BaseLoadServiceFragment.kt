package com.quenstin.basicmodel.view.fragment

import android.text.TextUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ToastUtils
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.quenstin.basicmodel.state.State
import com.quenstin.basicmodel.state.StateType
import com.quenstin.basicmodel.state.callback.*

/**
 * Created by hdyjzgq
 * data on 5/14/21
 * function is ：页面状态逻辑
 */
abstract class BaseLoadServiceFragment:Fragment() {



    /**
     * 注册加载框架
     * view状态,null 错误 正常
     */
    private val loadService: LoadService<*> by lazy {
        LoadSir.getDefault().register(this) {
            reload()
        }
    }

    /**
     * 重试请求，
     * 需要重试逻辑可以自己重写该方法处理相关逻辑
     */
    open fun reload() {}


    /**
     * 加载中
     */
    private fun showLoading() {
        loadService.showCallback(LoadingCallBack::class.java)
    }

    /**
     * 加载成功
     */
    private fun showSuccess() {
        loadService.showCallback(SuccessCallback::class.java)
    }

    /**
     * 加载失败
     * 0:网络异常
     * 1:解析异常
     * 2:其他异常
     */
    open fun showError(state: Int, msg: String) {
        if (msg.isNotEmpty()) {
            ToastUtils.showShort(msg)
        }
        when (state) {
            0 -> {
                loadService.showCallback(HttpErrorCallBack::class.java)
            }
            1 -> {
                loadService.showCallback(JsonErrorCallBack::class.java)
            }
            else -> {
                loadService.showCallback(ErrorCallBack::class.java)
            }
        }
    }

    /**
     * 提示信息
     */
    open fun showTip(msg: String) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
        loadService.showCallback(SuccessCallback::class.java)
    }

    /**
     * 空布局
     */
    open fun showEmpty() {
        loadService.showCallback(EmptyCallBack::class.java)
    }

    /**
     * 加载状态
     */
    val observer by lazy {
        Observer<State> {
            it?.let {
                when (it.code) {
                    StateType.SUCCESS -> showSuccess()
                    StateType.LOADING -> showLoading()
                    StateType.NETWORK_ERROR -> showError(0, "")
                    StateType.TIP -> showTip(it.message)
                    StateType.EMPTY -> showEmpty()
                    StateType.JSON -> showError(1, "")
                    StateType.TIMEOUT -> showError(2, it.message)
                    else -> showError(2, it.message)
                }
            }
        }
    }
}