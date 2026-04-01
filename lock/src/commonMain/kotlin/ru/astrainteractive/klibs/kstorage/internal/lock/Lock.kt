package ru.astrainteractive.klibs.kstorage.internal.lock

interface Lock {
    val isLocked: Boolean
    fun <T> withLock(block: () -> T): T
    suspend fun <T> withSuspendLock(block: suspend () -> T): T
}
