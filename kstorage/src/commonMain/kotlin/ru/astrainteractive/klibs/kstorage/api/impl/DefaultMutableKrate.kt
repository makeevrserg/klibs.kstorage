package ru.astrainteractive.klibs.kstorage.api.impl

import ru.astrainteractive.klibs.kstorage.api.Krate
import ru.astrainteractive.klibs.kstorage.api.provider.ValueFactory
import ru.astrainteractive.klibs.kstorage.api.provider.ValueLoader
import ru.astrainteractive.klibs.kstorage.api.provider.ValueSaver

/**
 * This [DefaultMutableKrate] can be used with delegation
 *
 * If false will put [factory] value into [cachedValue]
 */
class DefaultMutableKrate<T>(
    private val factory: ValueFactory<T>,
    private val saver: ValueSaver<T> = ValueSaver.Empty(),
    private val loader: ValueLoader<T>,
) : Krate.Mutable<T> {

    private var _cachedValue: T = loader.loadAndGet() ?: factory.create()

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
