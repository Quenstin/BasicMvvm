package com.quenstin.basicmodel.http.net.manager

import com.quenstin.basicmodel.http.net.livedata.EventLiveData

/**
 * Created by hdyjzgq
 * data on 4/12/21
 * function is ：管理网络变化
 */
class NetworkStateManager private constructor() {

    val mNetworkStateCallback = EventLiveData<NetState>()

    companion object {
        val instance: NetworkStateManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkStateManager()
        }
    }

}