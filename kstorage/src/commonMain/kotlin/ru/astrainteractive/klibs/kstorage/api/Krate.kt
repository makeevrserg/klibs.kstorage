package ru.astrainteractive.klibs.kstorage.api

/**
 * [Krate] is a wrapper for your favorite key-value storage library
 */
interface Krate<T> {
    /**
     * Current state of a [cachedValue]
     */
    val cachedValue: T

    /**
     * Load value from storage and update [cachedValue]
     */
    fun loadAndGet(): T
}
