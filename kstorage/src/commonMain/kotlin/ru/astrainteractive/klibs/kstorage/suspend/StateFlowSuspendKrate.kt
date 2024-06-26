package ru.astrainteractive.klibs.kstorage.suspend

import kotlinx.coroutines.flow.StateFlow
import ru.astrainteractive.klibs.kstorage.api.CachedKrate

interface StateFlowSuspendKrate<T> : SuspendKrate<T>, CachedKrate.Coroutine<T> {
    override val cachedStateFlow: StateFlow<T>
    override val cachedValue: T
        get() = cachedStateFlow.value

    interface Mutable<T> : StateFlowSuspendKrate<T>, SuspendKrate.Mutable<T> {
        override val cachedStateFlow: StateFlow<T>
        override val cachedValue: T
            get() = cachedStateFlow.value
    }
}
