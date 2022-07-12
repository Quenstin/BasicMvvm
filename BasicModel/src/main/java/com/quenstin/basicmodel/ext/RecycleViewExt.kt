package com.hdyj.basicmodel.ext

import androidx.recyclerview.widget.RecyclerView

/**
 * Created by hdyjzgq
 * data on 2021/9/19
 * function is ：recycle拓展函数
 */


fun RecyclerView.setDef(layout: RecyclerView.LayoutManager, mAdapter: RecyclerView.Adapter<*>) {
    overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    isNestedScrollingEnabled = false
    setHasFixedSize(true)
    layoutManager = layout
    adapter = mAdapter

}



