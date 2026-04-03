package ru.astrainteractive.klibs.kstorage.api

import ru.astrainteractive.klibs.kstorage.internal.lock.Lock

interface LockOwner {
    val lock: Lock
}

fun Any.reuseLock(): Lock {
    return (this as? LockOwner)?.lock ?: Lock()
}
