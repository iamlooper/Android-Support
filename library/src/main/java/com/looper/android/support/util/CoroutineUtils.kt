package com.looper.android.support.util

import kotlinx.coroutines.*

class CoroutineUtils {
    private var coroutineScope = createScope()

    private fun createScope() = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val jobMap: MutableMap<String, Job> = mutableMapOf()

    fun io(key: String, block: suspend CoroutineScope.() -> Unit): Job =
        coroutineScope.launch(Dispatchers.IO, block = block).apply {
            invokeOnCompletion { jobMap.remove(key) }
            jobMap[key] = this
        }

    fun main(key: String, block: suspend CoroutineScope.() -> Unit): Job =
        coroutineScope.launch(Dispatchers.Main, block = block).apply {
            invokeOnCompletion { jobMap.remove(key) }
            jobMap[key] = this
        }

    fun default(key: String, block: suspend CoroutineScope.() -> Unit): Job =
        coroutineScope.launch(Dispatchers.Default, block = block).apply {
            invokeOnCompletion { jobMap.remove(key) }
            jobMap[key] = this
        }

    fun cancelJob(key: String) {
        jobMap[key]?.let {
            it.cancel()
            jobMap.remove(key)
        }
    }

    fun cancelJobAfterAssignment(key: String) {
        synchronized(jobMap) {
            if (jobMap.containsKey(key)) {
                cancelJob(key)
            } else {
                coroutineScope.launch {
                    while (!jobMap.containsKey(key)) {
                        delay(100) // Delay a bit to wait for the job to be registered.
                    }
                    cancelJob(key)
                }
            }
        }
    }

    fun cancelAllJobs() {
        coroutineScope.cancel()
        jobMap.clear()
    }

    fun recreateScope() {
        coroutineScope = createScope()
    }
}