package ru.astrainteractive.klibs.kstorage.api.flow

import kotlinx.coroutines.flow.StateFlow
import ru.astrainteractive.klibs.kstorage.api.MutableKrate

/**
 * [StateFlowKrate] allows you to use your storage values in reactive way
 */
interface StateFlowMutableKrate<T> : MutableKrate<T>, StateFlowKrate<T> {
    override val cachedStateFlow: StateFlow<T>
    override val cachedValue: T
        get() = cachedStateFlow.value
}
