package ru.astrainteractive.klibs.kstorage.lock.api

@InternalLockApi
public interface Lock {
    public val isLocked: Boolean
    public fun <T> withLock(block: () -> T): T
    public suspend fun <T> withSuspendLock(block: suspend () -> T): T
}
