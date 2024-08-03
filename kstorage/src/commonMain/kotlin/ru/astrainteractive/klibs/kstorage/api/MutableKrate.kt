package ru.astrainteractive.klibs.kstorage.api

/**
 * [MutableKrate] allows you to save/load values from your storage without
 * depending on SharedPreferences or other library
 */
interface MutableKrate<T> : Krate<T> {
    /**
     * Save new value into storage and update current
     */
    fun save(value: T)

    /**
     * Reset value to default
     */
    fun reset()
}
