package ru.astrainteractive.klibs.kstorage.suspend

import ru.astrainteractive.klibs.kstorage.api.Krate

/**
 * Same as [Krate], [SuspendKrate] is a wrapper for your favorite key-value storage library
 */
interface SuspendKrate<T> {
    /**
     * Load value from storage and update [cachedValue]
     */
    suspend fun getValue(): T
}
