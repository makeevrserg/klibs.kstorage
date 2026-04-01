package ru.astrainteractive.klibs.kstorage.internal.lock

import kotlinx.coroutines.runBlocking
import platform.Foundation.NSLock

class AppleLock : Lock {
    private val nsLock = NSLock()
    override var isLocked: Boolean = false

    override fun <T> withLock(block: () -> T): T {
        nsLock.lock()
        isLocked = true
        return try {
            block()
        } finally {
            isLocked = false
            nsLock.unlock()
        }
    }

    override suspend fun <T> withSuspendLock(block: suspend () -> T): T {
        return withLock { runBlocking { block.invoke() } }
    }
}
