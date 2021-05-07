package com.quention.basicmvvm

import com.quenstin.basicmodel.utils.AppLoge
import com.quenstin.basicmodel.view.BaseActivity
import com.quention.basicmvvm.databinding.ActivityMainBinding
import com.quention.basicmvvm.viewModel.TestViewModel

class MainActivity : BaseActivity<TestViewModel, ActivityMainBinding>() {


    override fun initView() {

    }

    override fun initData() {
        mViewBinding.testButton.setOnClickListener {
            mViewModel.getBannerData()
        }
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




}

