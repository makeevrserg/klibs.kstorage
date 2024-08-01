package ru.astrainteractive.klibs.kstorage.api.impl

import ru.astrainteractive.klibs.kstorage.api.MutableKrate
import ru.astrainteractive.klibs.kstorage.api.cache.LoadingStarted
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import ru.astrainteractive.klibs.kstorage.api.value.ValueLoader
import ru.astrainteractive.klibs.kstorage.api.value.ValueSaver

/**
 * This [DefaultMutableKrate] can be used with delegation
 *
 * If false will put [factory] value into [cachedValue]
 */
class DefaultMutableKrate<T>(
    private val factory: ValueFactory<T>,
    private val saver: ValueSaver<T> = ValueSaver.Empty(),
    private val loader: ValueLoader<T>,
    loadingStarted: LoadingStarted = LoadingStarted.Instantly
) : MutableKrate<T> {

    private var _cachedValue: T = when (loadingStarted) {
        LoadingStarted.Instantly -> loader.loadAndGet() ?: factory.create()
        LoadingStarted.Manually -> factory.create()
    }

    override val cachedValue: T
        get() = _cachedValue

    override fun loadAndGet(): T {
        val newValue = loader.loadAndGet() ?: factory.create()
        _cachedValue = newValue
        return newValue
    }

    override fun save(value: T) {
        saver.save(value)
        _cachedValue = value
    }

    override fun reset() {
        val defaultValue = factory.create()
        save(defaultValue)
    }
}
