package ru.astrainteractive.klibs.kstorage.suspend.flow

import kotlinx.coroutines.flow.StateFlow
import ru.astrainteractive.klibs.kstorage.api.cache.StateFlowCacheOwner
import ru.astrainteractive.klibs.kstorage.suspend.SuspendKrate

interface StateFlowSuspendKrate<T> : SuspendKrate<T>, StateFlowCacheOwner<T> {
    override val cachedStateFlow: StateFlow<T>
    override val cachedValue: T
        get() = cachedStateFlow.value
}
