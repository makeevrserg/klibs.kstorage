package ru.astrainteractive.klibs.kstorage.api.impl

import ru.astrainteractive.klibs.kstorage.api.CachedKrate
import ru.astrainteractive.klibs.kstorage.api.Krate
import ru.astrainteractive.klibs.kstorage.api.LockOwner
import ru.astrainteractive.klibs.kstorage.api.reuseLock

class DefaultCachedKrate<T>(
    private val instance: Krate<T>,
) : CachedKrate<T>, LockOwner {
    override val lock = instance.reuseLock()
    private var _cachedValue = lock.withLock { instance.getValue() }
    override val cachedValue: T
        get() = _cachedValue

    override fun getValue(): T {
        return lock.withLock {
            val newValue = instance.getValue()
            _cachedValue = newValue
            newValue
        }
    }
}
