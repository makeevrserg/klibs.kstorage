package ru.astrainteractive.klibs.kstorage.api

/**
 * [Krate] is a wrapper for your favorite key-value storage library
 */
interface Krate<T> : CachedKrate<T> {

    /**
     * Load value from storage and update [cachedValue]
     */
    fun loadAndGet(): T

    /**
     * [Krate.Mutable] allows you to save/load values from your storage without
     * depending on SharedPreferences or other library
     */
    interface Mutable<T> : Krate<T> {
        /**
         * Save new value into storage and update current
         */
        fun save(value: T)

        /**
         * Reset value to default
         */
        fun reset()
    }
}
