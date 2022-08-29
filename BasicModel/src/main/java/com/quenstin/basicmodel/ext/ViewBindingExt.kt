
@file:JvmName("ViewBindingUtil")
@file:Suppress("UNCHECKED_CAST", "unused")
package com.quenstin.basicmodel.ext
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType


/**
 * Created by hdyjzgq
 * data on 3/29/21
 * function is ：viewBing工具类
 */


/**
 * activity和fragment中使用的
 */
@JvmName("inflateWithGeneric")
fun <VB : ViewBinding> Any.inflateBindingWithGeneric(layoutInflater: LayoutInflater): VB =
    withGenericBindingClass(this) { clazz ->
        clazz.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as VB
    }

@JvmName("inflateWithGeneric")
fun <VB : ViewBinding> Any.inflateBindingWithGeneric(layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean): VB =
    withGenericBindingClass(this) { clazz ->
        clazz.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
            .invoke(null, layoutInflater, parent, attachToParent) as VB
    }

@JvmName("inflateWithGeneric")
fun <VB : ViewBinding> Any.inflateBindingWithGeneric(parent: ViewGroup): VB =
    inflateBindingWithGeneric(LayoutInflater.from(parent.context), parent, false)

fun <VB : ViewBinding> Any.bindViewWithGeneric(view: View): VB =
    withGenericBindingClass(this) { clazz ->
        clazz.getMethod("bind", LayoutInflater::class.java).invoke(null, view) as VB
    }

private fun <VB : ViewBinding> withGenericBindingClass(any: Any, block: (Class<VB>) -> VB): VB {
    any.allParameterizedType.forEach { parameterizedType ->
        parameterizedType.actualTypeArguments.forEach {
            try {
                return block.invoke(it as Class<VB>)
            } catch (e: NoSuchMethodException) {
            }
        }
    }
    throw IllegalArgumentException("There is no generic of ViewBinding.")
}

private val Any.allParameterizedType: List<ParameterizedType>
    get() {
        val genericParameterizedType = mutableListOf<ParameterizedType>()
        var genericSuperclass = javaClass.genericSuperclass
        var superclass = javaClass.superclass
        while (superclass != null) {
            if (genericSuperclass is ParameterizedType) {
                genericParameterizedType.add(genericSuperclass)
            }
            genericSuperclass = superclass.genericSuperclass
            superclass = superclass.superclass
        }
        return genericParameterizedType
    }


/**
 * 根据传入的泛型获取对应的viewModel实体
 */
fun <VM> getVmClass(obj: Any): VM {
    return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as VM
}


/**
 * 根据传入的泛型获取Repository
 */
fun <T> getClass(t: Any): Class<T> {
    // 通过反射 获取父类泛型 (T) 对应 Class类
    return (t.javaClass.genericSuperclass as ParameterizedType)
        .actualTypeArguments[0]
            as Class<T>

}