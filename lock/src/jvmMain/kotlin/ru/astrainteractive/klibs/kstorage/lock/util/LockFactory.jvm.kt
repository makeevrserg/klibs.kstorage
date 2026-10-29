package ru.astrainteractive.klibs.kstorage.lock.util

import ru.astrainteractive.klibs.kstorage.lock.api.InternalLockApi
import ru.astrainteractive.klibs.kstorage.lock.api.Lock
import ru.astrainteractive.klibs.kstorage.lock.mutex.JvmLock

@InternalLockApi
public actual fun Lock(): Lock {
    return JvmLock()
}
