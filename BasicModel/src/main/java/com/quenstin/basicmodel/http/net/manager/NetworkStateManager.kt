package com.hdyj.basicmodel.http.net.manager

import com.hdyj.basicmodel.livedata.UnPeekLiveData

/**
 * Created by hdyjzgq
 * data on 4/12/21
 * function is ：管理网络变化
 */
class NetworkStateManager private constructor() {

    val mNetworkStateCallback = UnPeekLiveData<NetState>()

    companion object {
        val instance: NetworkStateManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkStateManager()
        }
    }

}