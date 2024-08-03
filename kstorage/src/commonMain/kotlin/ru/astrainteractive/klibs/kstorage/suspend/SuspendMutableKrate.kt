package ru.astrainteractive.klibs.kstorage.suspend

import ru.astrainteractive.klibs.kstorage.api.MutableKrate

/**
 * Same as [MutableKrate], [SuspendMutableKrate] allows you to save/load values from your storage without
 * depending on SharedPreferences or other library
 */
interface SuspendMutableKrate<T> : SuspendKrate<T> {
    /**
     * Save new value into storage and update current
     */
    suspend fun save(value: T)

    /**
     * Reset value to default
     */
    suspend fun reset()
}
