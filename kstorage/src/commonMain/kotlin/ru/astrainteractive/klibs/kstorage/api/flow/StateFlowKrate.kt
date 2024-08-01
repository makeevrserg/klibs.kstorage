package ru.astrainteractive.klibs.kstorage.api.flow

import kotlinx.coroutines.flow.StateFlow
import ru.astrainteractive.klibs.kstorage.api.Krate
import ru.astrainteractive.klibs.kstorage.api.cache.StateFlowCacheOwner

interface StateFlowKrate<T> : Krate<T>, StateFlowCacheOwner<T> {
    override val cachedStateFlow: StateFlow<T>
    override val cachedValue: T
        get() = cachedStateFlow.value
}
