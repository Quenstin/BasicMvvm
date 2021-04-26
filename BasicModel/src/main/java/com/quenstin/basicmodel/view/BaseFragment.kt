package com.quenstin.basicmodel.view

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.quenstin.basicmodel.state.State
import com.quenstin.basicmodel.state.StateType
import com.quenstin.basicmodel.utils.getVmClass
import com.quenstin.basicmodel.utils.inflateBindingWithGeneric
import com.quenstin.basicmodel.viewmodel.BaseViewModel
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.quenstin.basicmodel.state.callback.EmptyCallBack
import com.quenstin.basicmodel.state.callback.ErrorCallBack
import com.quenstin.basicmodel.state.callback.LoadingCallBack

/**
 * Created by hdyjzgq
 * data on 3/29/21
 * function is ：fragment基类
 */
abstract class BaseFragment<VM : BaseViewModel<*>, VB : ViewBinding> : Fragment() {
    lateinit var loadService: LoadService<*>

    lateinit var mViewBinding: VB
    lateinit var mViewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewBinding = inflateBindingWithGeneric(layoutInflater)
        loadService = LoadSir.getDefault().register(mViewBinding.root)

        return mViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = initViewModel()
        mViewModel.loadState.observe(viewLifecycleOwner, observer)
        initView()
    }

    /**
     * 当前视图的布局
     * 其实有了viewBind可以不绑定但是为了方便查看布局
     */
    abstract fun layoutId():Int

    protected abstract fun initView()

    abstract fun showLoading(message: String = "请求网络中...")

    abstract fun dismissLoading()

    /**
     * 加载中
     */
    private fun showLoading() {
        loadService.showCallback(LoadingCallBack::class.java)
    }

    /**
     * 加载成功
     */
    private fun showSuccess() {
        loadService.showCallback(SuccessCallback::class.java)
    }

    /**
     * 加载失败
     */
    private fun showError(msg: String) {
        if (!TextUtils.isEmpty(msg)) {

            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
        loadService.showCallback(ErrorCallBack::class.java)
    }

    /**
     * 提示信息
     */
    open fun showTip(msg: String) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
        loadService.showCallback(SuccessCallback::class.java)
    }

    /**
     * 空布局
     */
    open fun showEmpty() {
        loadService.showCallback(EmptyCallBack::class.java)
    }

    /**
     * 页面状态
     */
    private val observer by lazy {
        Observer<State> {
            it?.let {
                when (it.code) {
                    StateType.SUCCESS -> showSuccess()
                    StateType.LOADING -> showLoading()
                    StateType.ERROR -> showTip(it.message)
                    StateType.NETWORK_ERROR -> showError("网络异常")
                    StateType.TIP -> showTip(it.message)
                    StateType.EMPTY -> showEmpty()
                }
            }
        }
    }

    private fun initViewModel(): VM {
        return ViewModelProvider(this).get(getVmClass(this))

    }
}