package com.zhuinden.mvvmaacrxjavaretrofitroom.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified VM : ViewModel> Fragment.fragmentViewModels(crossinline factory: (SavedStateHandle) -> VM) = viewModels<VM>(
    factoryProducer = {
        object : AbstractSavedStateViewModelFactory(this, arguments) {
            override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
                if (modelClass == VM::class.java) {
                    @Suppress("UNCHECKED_CAST")
                    return factory(handle) as T
                }
                throw IllegalArgumentException("Unexpected argument: $modelClass")
            }
        }
    }
)

inline fun <reified T : ViewModel> AppCompatActivity.createViewModel(crossinline factory: (SavedStateHandle) -> T): T = T::class.java.let { clazz ->
    ViewModelProvider(this,
        object : AbstractSavedStateViewModelFactory(this, intent?.extras) {
            override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
                if (modelClass == clazz) {
                    @Suppress("UNCHECKED_CAST")
                    return factory(handle) as T
                }
                throw IllegalArgumentException("Unexpected argument: $modelClass")
            }
        }).get(clazz)
}

inline fun <reified T : ViewModel> Fragment.createViewModel(crossinline factory: (SavedStateHandle) -> T): T = T::class.java.let { clazz ->
    ViewModelProvider(this, object : AbstractSavedStateViewModelFactory(this, arguments) {
        override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
            if (modelClass == clazz) {
                @Suppress("UNCHECKED_CAST")
                return factory(handle) as T
            }
            throw IllegalArgumentException("Unexpected argument: $modelClass")
        }
    }).get(clazz)
}

tailrec fun <T : Activity> Context.findActivity(): T {
    if (this is Activity) {
        @Suppress("UNCHECKED_CAST")
        return this as T
    } else {
        val contextWrapper = this as ContextWrapper?
        val baseContext = contextWrapper!!.baseContext
            ?: throw IllegalStateException("Activity was not found as base context of view!")
        return baseContext.findActivity()
    }
}

fun <T: Activity> View.findActivity(): T = this.context.findActivity()

fun ViewGroup.inflate(@LayoutRes layout: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(this.context).inflate(layout, this, attachToRoot)
