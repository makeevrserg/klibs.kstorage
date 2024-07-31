package ru.astrainteractive.klibs.kstorage.api.cache

interface CacheOwner<T> {
    /**
     * Last loaded value
     */
    val cachedValue: T
}
