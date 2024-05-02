package ru.astrainteractive.klibs.kstorage.impl

import ru.astrainteractive.klibs.kstorage.api.MutableStorageValue
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import ru.astrainteractive.klibs.kstorage.api.value.ValueLoader
import ru.astrainteractive.klibs.kstorage.api.value.ValueSaver

/**
 * This DefaultStorageValue<T> can be used with delegation
 */
internal class MutableStorageValueImpl<T>(
    private val factory: ValueFactory<T>,
    private val saver: ValueSaver<T>,
    private val loader: ValueLoader<T>,
) : MutableStorageValue<T> {
    private var currentValue: T = loader.load()

    override val value: T
        get() = currentValue

    override fun load(): T {
        val newValue = loader.load()
        currentValue = newValue
        return newValue
    }

    override fun save(value: T) {
        saver.save(value)
        currentValue = value
    }

    override fun reset() {
        save(factory.create())
    }
}
