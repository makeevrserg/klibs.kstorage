package ru.astrainteractive.klibs.kstorage.api.cache

import kotlinx.coroutines.flow.StateFlow

/**
 * [StateFlowCacheOwner] will cache last loaded value into [StateFlowCacheOwner.cachedStateFlow]
 */
interface StateFlowCacheOwner<T> : CacheOwner<T> {
    /**
     * Current state of a [cachedValue]
     */
    val cachedStateFlow: StateFlow<T>

    override val cachedValue: T
        get() = cachedStateFlow.value
}
