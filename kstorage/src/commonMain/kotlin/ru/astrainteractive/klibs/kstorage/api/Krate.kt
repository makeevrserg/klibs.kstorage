package ru.astrainteractive.klibs.kstorage.api

/**
 * [Krate] is a wrapper for your favorite key-value storage library
 */
interface Krate<T> : CachedKrate<T> {

    /**
     * Load value from storage and update [cachedValue]
     */
    fun loadAndGet(): T
}
