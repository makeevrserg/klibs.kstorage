package ru.astrainteractive.klibs.kstorage.internal.lock

import kotlinx.cinterop.Arena
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.ptr
import platform.posix.pthread_mutex_destroy
import platform.posix.pthread_mutex_init
import platform.posix.pthread_mutex_lock
import platform.posix.pthread_mutex_t
import platform.posix.pthread_mutex_unlock
import platform.posix.pthread_mutexattr_destroy
import platform.posix.pthread_mutexattr_init
import platform.posix.pthread_mutexattr_settype
import platform.posix.pthread_mutexattr_t
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.ref.createCleaner

/**
 * @author https://github.com/arkivanov
 */
@OptIn(ExperimentalForeignApi::class)
class LinuxLock : Lock {
    private val resources = Resources()

    @Suppress("unused") // Must be assigned
    @OptIn(ExperimentalNativeApi::class)
    private val cleaner = createCleaner(resources, Resources::destroy)
    private fun lock() {
        pthread_mutex_lock(resources.mutex.ptr)
    }

    private fun unlock() {
        pthread_mutex_unlock(resources.mutex.ptr)
    }

    override fun <T> withLock(block: () -> T): T {
        lock()
        return try {
            block()
        } finally {
            unlock()
        }
    }

    private class Resources {
        private val arena = Arena()
        private val attr: pthread_mutexattr_t = arena.alloc()
        val mutex: pthread_mutex_t = arena.alloc()

        init {
            pthread_mutexattr_init(attr.ptr)
            pthread_mutexattr_settype(attr.ptr, PTHREAD_MUTEX_RECURSIVE)
            pthread_mutex_init(mutex.ptr, attr.ptr)
        }

        fun destroy() {
            pthread_mutex_destroy(mutex.ptr)
            pthread_mutexattr_destroy(attr.ptr)
            arena.clear()
        }
    }

    companion object {

        /**
         * Wrapper for platform.posix.PTHREAD_MUTEX_RECURSIVE which
         * is represented as kotlin.Int on darwin platforms and kotlin.UInt on linuxX64
         * See: // https://youtrack.jetbrains.com/issue/KT-41509
         * @author https://github.com/arkivanov
         */
        private val PTHREAD_MUTEX_RECURSIVE: Int get() = platform.posix.PTHREAD_MUTEX_RECURSIVE.toInt()
    }
}
