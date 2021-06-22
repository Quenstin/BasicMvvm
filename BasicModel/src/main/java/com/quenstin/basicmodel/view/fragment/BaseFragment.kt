package com.quenstin.basicmodel.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.quenstin.basicmodel.utils.inflateBindingWithGeneric

/**
 * Created by hdyjzgq
 * data on 3/29/21
 * function is ：fragment基类
 * tip：
 *      只封装了viewBing，如果是简单页面不需要处理耦合可继承该activity
 */
abstract class BaseFragment<VB : ViewBinding> : BaseLoadServiceFragment() {

    lateinit var mViewBinding: VB
   lateinit  var mContext:Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext=context
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewBinding=inflateBindingWithGeneric(layoutInflater,container,false)
        return mViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setListener()
        initView()
        initData()
    }

    /**
     * 初始化viewModel
     * 用于baseMVVMActivity
     * 如果没有继承可使用自带的拓展类 by viewModel()来获取
     */
    open fun initViewModel() {}


    /**
     * 初始化view
     */
    abstract fun initView()

    /**
     * 初始化监听
     */
    open fun setListener(){}

    /**
     * 初始化数据源or请求
     */
    abstract fun initData()





}