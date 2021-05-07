package com.quenstin.basicmodel.view

import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.quenstin.basicmodel.utils.getVmClass
import com.quenstin.basicmodel.viewmodel.BaseViewModel

/**
 * Created by hdyjzgq
 * data on 5/6/21
 * function is ：
 *  tip:
 *      适用于mvvm模式的activity
 *      增加了viewModel的封装
 */
abstract class BaseMVVMActivity<VM  : BaseViewModel<*>,VB : ViewBinding>: BaseActivity<VB>() {

    lateinit var mViewModel: VM

    override fun initViewModel() {
        mViewModel = ViewModelProvider(this).get(getVmClass(this))
        mViewModel.loadState.observe(this, observer)
    }




}