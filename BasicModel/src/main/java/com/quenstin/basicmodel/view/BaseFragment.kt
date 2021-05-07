package com.quenstin.basicmodel.view

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ToastUtils
import com.quenstin.basicmodel.state.State
import com.quenstin.basicmodel.state.StateType
import com.quenstin.basicmodel.utils.getVmClass
import com.quenstin.basicmodel.utils.inflateBindingWithGeneric
import com.quenstin.basicmodel.viewmodel.BaseViewModel
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.quenstin.basicmodel.state.callback.*

/**
 * Created by hdyjzgq
 * data on 3/29/21
 * function is ：fragment基类
 * tip：
 *      只封装了viewBing，如果是简单页面不需要处理耦合可继承该activity
 */
abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    lateinit var loadService: LoadService<*>

    lateinit var mViewBinding: VB


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewBinding = inflateBindingWithGeneric(layoutInflater)
        loadService = LoadSir.getDefault().register(mViewBinding.root)
        return mViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initView()
        initListener()
        initData()
    }

    /**
     * 初始化viewModel
     * 用于baseMVVMActivity
     * 如果没有继承可使用自带的拓展类 by viewModel()来获取
     */
    open fun initViewModel(){}


    /**
     * 初始化view
     */
    abstract fun initView()

    /**
     * 事件
     */
    abstract fun initListener()

    /**
     * 初始化数据源or请求
     */
    abstract fun initData()


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