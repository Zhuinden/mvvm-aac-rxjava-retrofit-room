package com.zhuinden.mvvmaacrxjavaretrofitroom.utils

import android.app.Activity
import android.arch.lifecycle.*
import android.content.Context
import android.content.ContextWrapper
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

inline fun <reified T: ViewModel> AppCompatActivity.createViewModel(crossinline factory: () -> T): T = T::class.java.let { clazz ->
    ViewModelProviders.of(this, object: ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass == clazz) {
                @Suppress("UNCHECKED_CAST")
                return factory() as T
            }
            throw IllegalArgumentException("Unexpected argument: $modelClass")
        }
    }).get(clazz)
}

inline fun <reified T: ViewModel> Fragment.createViewModel(crossinline factory: () -> T): T = T::class.java.let { clazz ->
    ViewModelProviders.of(this, object: ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass == clazz) {
                @Suppress("UNCHECKED_CAST")
                return factory() as T
            }
            throw IllegalArgumentException("Unexpected argument: $modelClass")
        }
    }).get(clazz)
}

fun <T> LiveData<T>.distinctUntilChanged(): LiveData<T> = MediatorLiveData<T>().also { mediator ->
    mediator.addSource(this, object : Observer<T> {
        private var isInitialized = false
        private var previousValue: T? = null

        override fun onChanged(newValue: T?) {
            val wasInitialized = isInitialized
            if (!isInitialized) {
                isInitialized = true
            }
            if(!wasInitialized || newValue != previousValue) {
                previousValue = newValue
                mediator.postValue(newValue)
            }
        }
    })
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
