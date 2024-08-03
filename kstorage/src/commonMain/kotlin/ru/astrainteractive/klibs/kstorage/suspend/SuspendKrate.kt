package ru.astrainteractive.klibs.kstorage.suspend

import ru.astrainteractive.klibs.kstorage.api.Krate
import ru.astrainteractive.klibs.kstorage.api.cache.CacheOwner

/**
 * Same as [Krate], [SuspendKrate] is a wrapper for your favorite key-value storage library
 */
interface SuspendKrate<T> : CacheOwner<T> {
    /**
     * Load value from storage and update [cachedValue]
     */
    suspend fun loadAndGet(): T
}
