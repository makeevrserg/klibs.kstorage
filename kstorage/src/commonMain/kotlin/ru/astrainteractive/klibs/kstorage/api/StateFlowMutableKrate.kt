package ru.astrainteractive.klibs.kstorage.api

import kotlinx.coroutines.flow.StateFlow

/**
 * [StateFlowMutableKrate] allows you to use your storage values in reactive way
 */
interface StateFlowMutableKrate<T> : MutableKrate<T>, StateFlowKrate<T> {
    override val cachedStateFlow: StateFlow<T>

    override val cachedValue: T
        get() = cachedStateFlow.value
}
