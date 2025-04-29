package ru.astrainteractive.klibs.kstorage.api.impl

import ru.astrainteractive.klibs.kstorage.api.MutableKrate
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import ru.astrainteractive.klibs.kstorage.api.value.ValueLoader
import ru.astrainteractive.klibs.kstorage.api.value.ValueSaver

/**
 * This [DefaultMutableKrate] can be used with delegation
 */
class DefaultMutableKrate<T>(
    private val factory: ValueFactory<T>,
    private val saver: ValueSaver<T> = ValueSaver.Empty(),
    private val loader: ValueLoader<T>,
) : MutableKrate<T> {
    override fun getValue(): T {
        val newValue = loader.loadAndGet() ?: factory.create()
        return newValue
    }

    override fun save(value: T) {
        saver.save(value)
    }

    override fun reset() {
        val defaultValue = factory.create()
        save(defaultValue)
    }
}
