package fr.josstoh.letsvote.config

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Shared executors for use throughout the app by way of dependency injection.
 */

class AppExecutors internal constructor() {

    companion object {
        private const val NUM_NETWORK_THREADS = 3
        private val NUM_CPU_THREADS = Math.max(1, Runtime.getRuntime().availableProcessors() - 1)
        val instance: AppExecutors by lazy { AppExecutors() }
    }

    /** For work that's bound to local disk activity. */
    val diskExecutorService: ExecutorService by lazy { Executors.newSingleThreadExecutor() }
    /** For work that's bound to networking. */
    val networkExecutorService: ExecutorService by lazy {
        Executors.newFixedThreadPool(
            NUM_NETWORK_THREADS
        )
    }
    /** For work that's bound to CPU computation. */
    val cpuExecutorService: ExecutorService by lazy { Executors.newFixedThreadPool(NUM_CPU_THREADS) }
    /** For work that must run on the main thread. */
    val mainExecutor by lazy { MainThreadExecutor() }

    class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}
