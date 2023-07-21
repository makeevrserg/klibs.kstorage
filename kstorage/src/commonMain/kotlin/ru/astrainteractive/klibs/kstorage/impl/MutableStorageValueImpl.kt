package ru.astrainteractive.klibs.kstorage.impl

import ru.astrainteractive.klibs.kstorage.api.MutableStorageValue

/**
 * This DefaultStorageValue<T> can be used with delegation
 */
internal class MutableStorageValueImpl<T>(
    private val default: T,
    private val loadSettingsValue: () -> T,
    private val saveSettingsValue: (T) -> Unit
) : MutableStorageValue<T> {
    private var currentValue: T = loadSettingsValue.invoke()
    override val value: T
        get() = currentValue

    override fun load(): T {
        val newValue = loadSettingsValue.invoke()
        currentValue = newValue
        return newValue
    }

    override fun save(value: T) {
        saveSettingsValue.invoke(value)
        currentValue = value
    }

    override fun reset() {
        save(default)
    }

    override fun update(block: (T) -> T) {
        save(block(value))
    }
}
