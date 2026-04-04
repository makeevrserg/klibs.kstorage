package ru.astrainteractive.klibs.kstorage.internal.lock

class JsLock : Lock {
    override var isLocked: Boolean = false
    override fun <T> withLock(block: () -> T): T {
        isLocked = true
        val value = block.invoke()
        isLocked = false
        return value
    }

    override suspend fun <T> withSuspendLock(block: suspend () -> T): T {
        isLocked = true
        val value = block.invoke()
        isLocked = false
        return value
    }
}
