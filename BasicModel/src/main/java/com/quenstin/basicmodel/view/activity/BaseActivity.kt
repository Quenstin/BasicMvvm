package com.quenstin.basicmodel.view.activity

import android.content.Context
import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.quenstin.basicmodel.ext.initBar
import com.quenstin.basicmodel.utils.inflateBindingWithGeneric

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
abstract class BaseActivity<VB : ViewBinding> : BaseLoadServiceActivity() {

    lateinit var mViewBinding: VB
    lateinit var mContext: Context



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext=this
        mViewBinding = inflateBindingWithGeneric(layoutInflater)
        initWindow()
        setContentView(mViewBinding.root)
        init()
    }

    private fun init() {
        initViewModel()
        initBar()
        initView()
        initData()
        initListener()
        initObserver()

    }

    /**
     * 初始化view
     */
    abstract fun initView()


    /**
     * 初始化监听
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


    /**
     * 初始化viewModel
     * 用于baseMVVMActivity
     * 如果没有继承可使用自带的拓展类 by viewModel()来获取
     */
    open fun initViewModel() {}


    /**
     * 需要设置window参数可重写此方法
     */
    open fun initWindow() {}


}


