package com.quention.basicmvvm

import com.quenstin.basicmodel.utils.AppLoge
import com.quenstin.basicmodel.view.BaseActivity
import com.quention.basicmvvm.databinding.ActivityMainBinding
import com.quention.basicmvvm.viewModel.TestViewModel

class MainActivity : BaseActivity<TestViewModel, ActivityMainBinding>() {
    override fun layoutId() = R.layout.activity_main

    override fun initView() {

    }

    override fun initData() {
        mViewModel.getHomeData("12")
    }

    override fun initObserver() {
        mViewModel.data.observe(this, { t ->
            t?.let {
                AppLoge(it.toString())
            }
        })
    }


}