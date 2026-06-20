package ru.astrainteractive.klibs.kstorage.internal.lock

class JsLock : Lock {
    private var lockDepth = 0

    override val isLocked: Boolean
        get() = lockDepth > 0

    override fun <T> withLock(block: () -> T): T {
        lockDepth += 1
        return try {
            block.invoke()
        } finally {
            lockDepth -= 1
        }
    }

    override suspend fun <T> withSuspendLock(block: suspend () -> T): T {
        lockDepth += 1
        return try {
            block.invoke()
        } finally {
            lockDepth -= 1
        }
    }
}
