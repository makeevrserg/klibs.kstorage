package ru.astrainteractive.klibs.kstorage.api

import kotlin.reflect.KProperty

/**
 * Represents a Krate that provides access to a cached value.
 * This interface allows you to access the stored value directly without needing to reload it from the source,
 * providing faster retrieval for frequently used data.
 */
interface CachedKrate<T> : Krate<T> {

    /**
     * The cached value stored in the Krate, which can be accessed directly.
     */
    val cachedValue: T
}

/**
 * Allows Kotlin's property delegation syntax to access the cached value of a CachedKrate.
 */
operator fun <T> CachedKrate<T>.getValue(thisRef: Any, property: KProperty<*>): T {
    return this.cachedValue
}

/**
 * Allows Kotlin's property delegation syntax to access the cached value of a CachedKrate.
 */
operator fun <T> CachedKrate<T>.getValue(thisRef: Nothing?, property: KProperty<*>): T {
    return this.cachedValue
}
