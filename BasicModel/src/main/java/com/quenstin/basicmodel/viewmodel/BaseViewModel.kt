package com.hdyj.basicmodel.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hdyj.basicmodel.repository.BaseRepository
import com.hdyj.basicmodel.state.State
import com.hdyj.basicmodel.ext.getClass

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