package com.quention.basicmvvm

import com.quenstin.basicmodel.ext.setActivityTitle
import com.quenstin.basicmodel.utils.AppLoge
import com.quenstin.basicmodel.view.activity.BaseMVVMActivity
import com.quention.basicmvvm.databinding.ActivityMainBinding
import com.quention.basicmvvm.viewModel.TestViewModel

class MainActivity : BaseMVVMActivity<TestViewModel, ActivityMainBinding>() {


    override fun initView() {
        setActivityTitle("测试标题")

    }

    override fun initData() {

        mViewModel.getBannerData()
    }

    override fun initObserver() {

    }

    override fun initListener() {
        mViewBinding.testButton.setOnClickListener {
            mViewModel.getBannerData()
        }
    }


}

