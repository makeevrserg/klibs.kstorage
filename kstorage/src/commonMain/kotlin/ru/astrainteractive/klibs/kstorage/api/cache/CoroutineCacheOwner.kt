package ru.astrainteractive.klibs.kstorage.api.cache

import kotlinx.coroutines.flow.StateFlow

interface CoroutineCacheOwner<T> : CacheOwner<T> {
    /**
     * Current state of a [cachedValue]
     */
    val cachedStateFlow: StateFlow<T>

    override val cachedValue: T
        get() = cachedStateFlow.value
}
