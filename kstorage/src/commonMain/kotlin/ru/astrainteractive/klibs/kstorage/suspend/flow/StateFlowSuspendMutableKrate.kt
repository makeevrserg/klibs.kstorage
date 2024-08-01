package ru.astrainteractive.klibs.kstorage.suspend.flow

import kotlinx.coroutines.flow.StateFlow
import ru.astrainteractive.klibs.kstorage.suspend.SuspendMutableKrate

interface StateFlowSuspendMutableKrate<T> : StateFlowSuspendKrate<T>, SuspendMutableKrate<T> {
    override val cachedStateFlow: StateFlow<T>
    override val cachedValue: T
        get() = cachedStateFlow.value
}
