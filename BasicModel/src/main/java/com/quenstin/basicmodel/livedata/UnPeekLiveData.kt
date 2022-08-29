package com.quenstin.basicmodel.livedata


/**
 * Created by hdyjzgq
 * data on 6/9/21
 * function is ：
 */
class UnPeekLiveData<T> : ProtectedUnPeekLiveData<T>() {
    public override fun setValue(value: T?) {
        super.setValue(value)
    }


    class Builder<T> {
        /**
         * 是否允许传入 null value
         */
        private var isAllowNullValue = false
        fun setAllowNullValue(allowNullValue: Boolean): Builder<T> {
            isAllowNullValue = allowNullValue
            return this
        }

        fun create(): UnPeekLiveData<T> {
            val liveData = UnPeekLiveData<T>()
            liveData.isAllowNullValue = isAllowNullValue
            return liveData
        }
    }
}