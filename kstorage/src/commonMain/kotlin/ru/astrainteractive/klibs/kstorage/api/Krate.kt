package ru.astrainteractive.klibs.kstorage.api

import ru.astrainteractive.klibs.kstorage.api.cache.CacheOwner

/**
 * [Krate] is a wrapper for your favorite key-value storage library
 */
interface Krate<T> : CacheOwner<T> {

    /**
     * Load value from storage and update [cachedValue]
     */
    fun loadAndGet(): T
}
