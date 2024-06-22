package ru.astrainteractive.klibs.kstorage.api

import kotlinx.coroutines.flow.StateFlow

interface CachedKrate<T> {
    /**
     * Last loaded value
     */
    val cachedValue: T

    interface Coroutine<T> : CachedKrate<T> {
        /**
         * Current state of a [cachedValue]
         */
        val cachedStateFlow: StateFlow<T>
    }
}
