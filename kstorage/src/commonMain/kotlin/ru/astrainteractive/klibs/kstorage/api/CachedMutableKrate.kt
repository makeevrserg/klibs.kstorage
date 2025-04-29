package ru.astrainteractive.klibs.kstorage.api

/**
 * Represents a mutable Krate that combines both caching and modification capabilities.
 * This interface allows access to the cached value while also supporting saving and resetting the stored value.
 */
interface CachedMutableKrate<T> : CachedKrate<T>, MutableKrate<T>
