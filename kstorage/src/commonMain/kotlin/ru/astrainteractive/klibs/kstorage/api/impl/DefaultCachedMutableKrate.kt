package ru.astrainteractive.klibs.kstorage.api.impl

import ru.astrainteractive.klibs.kstorage.api.CachedMutableKrate
import ru.astrainteractive.klibs.kstorage.api.MutableKrate
import ru.astrainteractive.klibs.kstorage.internal.lock.Lock

class DefaultCachedMutableKrate<T>(
    private val instance: MutableKrate<T>,
) : CachedMutableKrate<T> {
    private val lock = Lock()
    private var _cachedValue = lock.withLock { instance.getValue() }
    override val cachedValue: T
        get() = _cachedValue

    override fun save(value: T) {
        lock.withLock {
            _cachedValue = value
            instance.save(value)
        }
    }

    override fun reset() {
        lock.withLock {
            instance.reset()
            _cachedValue = instance.getValue()
        }
    }

    override fun resetAndGet(): T {
        return lock.withLock {
            val currentValue = instance.resetAndGet()
            _cachedValue = currentValue
            currentValue
        }
    }

    override fun save(block: (T) -> T) {
        lock.withLock {
            val currentValue = instance.saveAndGet(block)
            _cachedValue = currentValue
        }
    }

    override fun saveAndGet(block: (T) -> T): T {
        return lock.withLock {
            val currentValue = instance.saveAndGet(block)
            _cachedValue = currentValue
            currentValue
        }
    }

    override fun getValue(): T {
        return lock.withLock {
            val value = instance.getValue()
            _cachedValue = value
            value
        }
    }
}
