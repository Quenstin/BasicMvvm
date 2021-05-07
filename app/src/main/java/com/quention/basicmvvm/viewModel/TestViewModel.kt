package com.quention.basicmvvm.viewModel

import androidx.lifecycle.MutableLiveData
import com.quenstin.basicmodel.ext.simpleLaunch
import com.quenstin.basicmodel.viewmodel.BaseViewModel
import com.quention.basicmvvm.data.TestBean
import com.quention.basicmvvm.repository.TestRepository

/**
 * Created by hdyjzgq
 * data on 4/26/21
 * function is ：
 */
class TestViewModel:BaseViewModel<TestRepository>() {

    val data = MutableLiveData<TestBean>()

    fun getBannerData() {
        simpleLaunch(request = {
            data.value = mRepository.getBanner()
        })
    }
}