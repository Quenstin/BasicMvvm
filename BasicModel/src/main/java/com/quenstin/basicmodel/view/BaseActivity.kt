package com.quenstin.basicmodel.view

import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ToastUtils
import com.example.basicmodel.R
import com.quenstin.basicmodel.state.State
import com.quenstin.basicmodel.state.StateType
import com.quenstin.basicmodel.state.callback.*
import com.quenstin.basicmodel.utils.getVmClass
import com.quenstin.basicmodel.utils.inflateBindingWithGeneric
import com.quenstin.basicmodel.viewmodel.BaseViewModel
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir

/**
 * Created by hdyjzgq
 * data on 3/29/21
 * function is ：activity基类
 *
 *  tip：
 *      只封装了viewBing，如果是简单页面不需要处理耦合可继承该activity
 *
 *  ps： 没有dataBing，想用dataBing不如学习Compose
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    lateinit var mViewBinding: VB


    /**
     * 注册加载框架
     * view状态,null 错误 正常
     */
    private val loadService: LoadService<*> by lazy {
        LoadSir.getDefault().register(this) {
            reLoad()
        }
    }

    open fun reLoad() {}


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
     * 订阅liveData
     */
    abstract fun initObserver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = inflateBindingWithGeneric(layoutInflater)
        setContentView(mViewBinding.root)
        init()
    }

    private fun init() {
        initViewModel()
        initView()
        initListener()
        initData()
        initObserver()
    }

    /**
     * 初始化viewModel
     * 用于baseMVVMActivity
     * 如果没有继承可使用自带的拓展类 by viewModel()来获取
     */
    open fun initViewModel() {}


    /**
     * 加载中
     */
    open fun showLoading() {
        loadService.showCallback(LoadingCallBack::class.java)
    }

    /**
     * 加载成功
     */
    open fun showSuccess() {
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
            ToastUtils.showShort(msg)
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


    fun setTitleView(title: String) {
        val textTitle = findViewById<TextView>(R.id.BasicViewTitle)
        if (textTitle != null) {
            textTitle.text = title
        }
    }

}


