package ru.astrainteractive.klibs.kstorage.lock.util

import ru.astrainteractive.klibs.kstorage.lock.api.InternalLockApi
import ru.astrainteractive.klibs.kstorage.lock.api.Lock
import ru.astrainteractive.klibs.kstorage.lock.mutex.LinuxLock

@InternalLockApi
public actual fun Lock(): Lock {
    return LinuxLock()
}
