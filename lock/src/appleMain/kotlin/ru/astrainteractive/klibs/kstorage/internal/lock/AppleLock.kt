package ru.astrainteractive.klibs.kstorage.internal.lock

import platform.Foundation.NSLock

class AppleLock : Lock {
    private val nsLock = NSLock()

    override fun <T> withLock(block: () -> T): T {
        nsLock.lock()
        return try {
            block()
        } finally {
            nsLock.unlock()
        }
    }
}
