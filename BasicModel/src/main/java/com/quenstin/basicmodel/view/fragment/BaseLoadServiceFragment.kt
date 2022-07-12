package com.hdyj.basicmodel.view.fragment

import android.text.TextUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.hdyj.basicmodel.http.net.manager.NetState
import com.hdyj.basicmodel.state.State
import com.hdyj.basicmodel.state.StateType
import com.hdyj.basicmodel.state.callback.*
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir

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
        LoadSir.getDefault().register(setLoadSirView()) {
            reload()
        }
    }


    /**
     * 此方法是属于亡羊补牢的措施
     * 因为LoadSir框架加载多状态的时候传入this会全屏幕被覆盖，
     * 按照官网来说是在baseActivity中默认加载一个带有title的布局和一个空的布局这样加载后也可以响应back
     * 但是修改基类改动太大，所以增加这个方法默认返回this，如果是对部分布局显示多状态可以在实现类中选择性的重写改方法进行重新注入view
     */
    protected open fun setLoadSirView(): Any = activity as Any

    /**
     * 重试请求，
     * 需要重试逻辑可以自己重写该方法处理相关逻辑
     */
    open fun reload() {}


    /**
     * 加载中
     */
     fun showLoading() {
        loadService.showCallback(LoadingCallBack::class.java)
    }

    /**
     * 加载成功
     */
     fun showSuccess() {
        loadService.showCallback(SuccessCallback::class.java)
    }

    /**
     * 加载失败
     * 0:网络异常
     * 1:解析异常
     * 2:其他异常
     */
    open fun showError(state:StateType) {

        when (state) {
            StateType.NETWORK_ERROR -> {
                loadService.showCallback(HttpErrorCallBack::class.java)
            }
            StateType.JSON -> {
                loadService.showCallback(JsonErrorCallBack::class.java)
            }
            StateType.TIMEOUT ->{
                loadService.showCallback(TimeOutErrorCallBack::class.java)
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
     * 传参错误 404
     */
    open fun showHttp404Error(){
        loadService.showCallback(Http404ErrorCallBack::class.java)
    }

    /**
     * 无网络通知
     */
    open fun showHttpNotError(){
        loadService.showCallback(NotWorkCallBack::class.java)
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
                    StateType.NETWORK_ERROR -> showError(StateType.NETWORK_ERROR)
                    StateType.TIP -> showTip(it.message)
                    StateType.EMPTY -> showEmpty()
                    StateType.JSON -> showError(StateType.JSON)
                    StateType.TIMEOUT -> showError(StateType.TIMEOUT)
                    StateType.ERROR_404 -> showHttp404Error()
                    StateType.NET_NOT -> showHttpNotError()
                    else -> showError(StateType.ERROR)
                }
            }
        }
    }

//    /**
//     * 监听网络状态
//     */
//    val netObserver by lazy {
//        Observer<NetState> {
//            if (it.isSuccess){
//                showSuccess()
//            }else{
//                showHttpNotError()
//            }
//        }
//    }
}