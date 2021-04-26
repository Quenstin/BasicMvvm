package com.quenstin.basicmodel.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.quenstin.basicmodel.repository.BaseRepository
import com.quenstin.basicmodel.state.State
import com.quenstin.basicmodel.utils.getClass

/**
 * Created by hdyjzgq
 * data on 3/29/21
 * function is ：viewModel基础类
 */
open class BaseViewModel<T : BaseRepository> : ViewModel() {

    /**
     * 页面状态
     */
    val loadState by lazy {
        MutableLiveData<State>()
    }


    /**
     * 反射获取repository
     */
    val mRepository: T by lazy {
        getClass<T>(this).newInstance()

    }
}