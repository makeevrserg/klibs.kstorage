package ru.astrainteractive.klibs.kstorage.api.cache

/**
 * [CacheOwner] will cache latest loaded value or default
 */
interface CacheOwner<T> {
    /**
     * Last loaded value
     */
    val cachedValue: T
}
