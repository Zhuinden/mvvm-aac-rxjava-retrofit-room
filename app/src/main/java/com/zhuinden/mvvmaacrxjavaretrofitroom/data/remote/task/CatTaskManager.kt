package com.zhuinden.mvvmaacrxjavaretrofitroom.data.remote.task

import java.util.*

class CatTaskManager {
    companion object {
        const val SAVE_CATS_TASK = "SAVE_CATS_TASK"
    }

    private val map: MutableMap<String, Boolean> = Collections.synchronizedMap(mutableMapOf())

    fun registerTask(taskTag: String) {
        map[taskTag] = true
    }

    fun unregisterTask(taskTag: String) {
        map[taskTag] = false
    }

    fun isRegistered(taskTag: String): Boolean {
        return map[taskTag] ?: false
    }
}