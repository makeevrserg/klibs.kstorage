package ru.astrainteractive.klibs.kstorage.internal.lock

import kotlinx.coroutines.runBlocking
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class JvmLock : Lock {
    private val reentrantLock = ReentrantLock()
    override var isLocked: Boolean = false

    override fun <T> withLock(block: () -> T): T {
        return reentrantLock.withLock {
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
