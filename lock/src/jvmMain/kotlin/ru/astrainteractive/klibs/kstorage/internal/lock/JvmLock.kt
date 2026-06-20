package ru.astrainteractive.klibs.kstorage.internal.lock

import kotlinx.coroutines.runBlocking
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class JvmLock : Lock {
    private val reentrantLock = ReentrantLock()

    override val isLocked: Boolean
        get() = reentrantLock.isLocked

    override fun <T> withLock(block: () -> T): T {
        return reentrantLock.withLock {
            block.invoke()
        }
    }

    override suspend fun <T> withSuspendLock(block: suspend () -> T): T {
        return withLock { runBlocking { block.invoke() } }
    }
}
