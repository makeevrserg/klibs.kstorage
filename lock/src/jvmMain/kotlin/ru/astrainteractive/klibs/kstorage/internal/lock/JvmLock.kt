package ru.astrainteractive.klibs.kstorage.internal.lock

class JvmLock : Lock {
    override fun <T> withLock(block: () -> T): T {
        return synchronized(this, block)
    }
}
