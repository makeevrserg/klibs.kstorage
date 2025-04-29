package ru.astrainteractive.klibs.kstorage.api.impl

import ru.astrainteractive.klibs.kstorage.api.CachedKrate
import ru.astrainteractive.klibs.kstorage.api.Krate

class DefaultCachedKrate<T>(
    private val instance: Krate<T>,
) : CachedKrate<T> {
    private var _cachedValue = instance.getValue()
    override val cachedValue: T
        get() = _cachedValue

    override fun getValue(): T {
        val value = instance.getValue()
        _cachedValue = value
        return value
    }
}
