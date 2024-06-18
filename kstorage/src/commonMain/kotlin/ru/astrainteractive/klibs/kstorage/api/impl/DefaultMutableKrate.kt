package ru.astrainteractive.klibs.kstorage.api.impl

import ru.astrainteractive.klibs.kstorage.api.MutableKrate
import ru.astrainteractive.klibs.kstorage.api.provider.ValueFactory
import ru.astrainteractive.klibs.kstorage.api.provider.ValueLoader
import ru.astrainteractive.klibs.kstorage.api.provider.ValueSaver

/**
 * This [DefaultMutableKrate] can be used with delegation
 *
 * @param requireInstantLoading - if true, will load value into [cachedValue] directly from [loader].
 * If false will put [factory] value into [cachedValue]
 */
class DefaultMutableKrate<T>(
    private val factory: ValueFactory<T>,
    private val saver: ValueSaver<T> = ValueSaver.Empty(),
    private val loader: ValueLoader<T>,
    private val requireInstantLoading: Boolean = true
) : MutableKrate<T> {

    private var _cachedValue: T = when {
        requireInstantLoading -> loader.loadAndGet() ?: factory.create()
        else -> factory.create()
    }

    override val cachedValue: T
        get() = _cachedValue

    override fun loadAndGet(): T {
        val newValue = loader.loadAndGet() ?: factory.create()
        _cachedValue = newValue
        return newValue
    }

    override fun save(value: T) {
        if (saver is ValueSaver.Empty) return
        saver.save(value)
        _cachedValue = value
    }

    override fun reset() {
        val defaultValue = factory.create()
        save(defaultValue)
    }
}
