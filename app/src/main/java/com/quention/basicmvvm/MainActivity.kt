package com.quention.basicmvvm

import com.quenstin.basicmodel.utils.AppLoge
import com.quenstin.basicmodel.view.BaseActivity
import com.quenstin.basicmodel.view.BaseMVVMActivity
import com.quention.basicmvvm.databinding.ActivityMainBinding
import com.quention.basicmvvm.viewModel.TestViewModel

class MainActivity : BaseMVVMActivity<TestViewModel, ActivityMainBinding>() {


    override fun initView() {
        setTitleView("测试标题")

    }

    override fun initData() {

        mViewModel.getBannerData()
    }

    override fun initObserver() {
        mViewModel.data.observe(this, { t ->
            t?.let {
                mViewBinding.testTv.text = it.toString()
                AppLoge(it.toString())
            }
        })
    }

    override fun initListener() {
        mViewBinding.testButton.setOnClickListener {
            mViewModel.getBannerData()
        }
    }


}

