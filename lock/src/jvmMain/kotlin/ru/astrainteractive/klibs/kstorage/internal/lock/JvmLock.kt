package ru.astrainteractive.klibs.kstorage.internal.lock

import kotlinx.coroutines.runBlocking

class JvmLock : Lock {
    override var isLocked: Boolean = false

    override fun <T> withLock(block: () -> T): T {
        return synchronized(this) {
            isLocked = true
            val value = block.invoke()
            isLocked = false
            value
        }
    }

    override suspend fun <T> withSuspendLock(block: suspend () -> T): T {
        return withLock { runBlocking { block.invoke() } }
    }
}
