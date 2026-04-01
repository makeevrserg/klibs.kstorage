package ru.astrainteractive.klibs.kstorage.api.impl

import ru.astrainteractive.klibs.kstorage.api.CachedKrate
import ru.astrainteractive.klibs.kstorage.api.Krate
import ru.astrainteractive.klibs.kstorage.internal.lock.Lock

class DefaultCachedKrate<T>(
    private val instance: Krate<T>,
) : CachedKrate<T> {
    private val lock = Lock()
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
