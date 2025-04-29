package ru.astrainteractive.klibs.kstorage.api

import kotlinx.coroutines.flow.StateFlow

interface StateFlowKrate<T> : CachedKrate<T> {
    val cachedStateFlow: StateFlow<T>
}
