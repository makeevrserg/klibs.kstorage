package ru.astrainteractive.klibs.kstorage.api.impl

import ru.astrainteractive.klibs.kstorage.api.CachedMutableKrate
import ru.astrainteractive.klibs.kstorage.api.MutableKrate

class DefaultCachedMutableKrate<T>(
    private val instance: MutableKrate<T>,
) : CachedMutableKrate<T> {
    private var _cachedValue = instance.getValue()
    override val cachedValue: T
        get() = _cachedValue

    override fun save(value: T) {
        _cachedValue = value
        instance.save(value)
    }

    override fun reset() {
        instance.reset()
        _cachedValue = instance.getValue()
    }

    override fun getValue(): T {
        val value = instance.getValue()
        _cachedValue = value
        return value
    }
}
