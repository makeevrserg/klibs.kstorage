package ru.astrainteractive.klibs.kstorage.api

/**
 * [StorageValue] is a wrapper for your favorite key-value storage library
 */
interface StorageValue<T> {
    /**
     * Current state of a [value]
     */
    val value: T

    /**
     * Load value from storage and update [value]
     */
    fun load(): T
}
