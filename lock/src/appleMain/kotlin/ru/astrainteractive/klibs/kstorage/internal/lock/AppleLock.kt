package ru.astrainteractive.klibs.kstorage.internal.lock

import kotlinx.coroutines.runBlocking
import platform.Foundation.NSRecursiveLock
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.decrementAndFetch
import kotlin.concurrent.atomics.incrementAndFetch

@OptIn(ExperimentalAtomicApi::class)
class AppleLock : Lock {
    private val nsLock = NSRecursiveLock()

    private val lockDepth = AtomicInt(0)

    override val isLocked: Boolean
        get() = lockDepth.load() > 0

    override fun <T> withLock(block: () -> T): T {
        nsLock.lock()
        lockDepth.incrementAndFetch()
        return try {
            block()
        } finally {
            lockDepth.decrementAndFetch()
            nsLock.unlock()
        }
    }

    override suspend fun <T> withSuspendLock(block: suspend () -> T): T {
        return withLock { runBlocking { block.invoke() } }
    }
}
