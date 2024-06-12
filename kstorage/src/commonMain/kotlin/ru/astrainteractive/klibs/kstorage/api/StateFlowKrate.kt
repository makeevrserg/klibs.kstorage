package ru.astrainteractive.klibs.kstorage.api

import kotlinx.coroutines.flow.StateFlow

interface StateFlowKrate<T> : Krate<T> {
    val cachedStateFlow: StateFlow<T>

    override val cachedValue: T
        get() = cachedStateFlow.value
}
