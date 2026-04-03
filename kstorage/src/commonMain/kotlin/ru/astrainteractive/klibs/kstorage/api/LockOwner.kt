package ru.astrainteractive.klibs.kstorage.api

import ru.astrainteractive.klibs.kstorage.internal.lock.Lock

internal interface LockOwner {
    val lock: Lock
}

internal fun Any.reuseLock(): Lock {
    return (this as? LockOwner)?.lock ?: Lock()
}
