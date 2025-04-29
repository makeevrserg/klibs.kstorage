package ru.astrainteractive.klibs.kstorage.suspend

import kotlinx.coroutines.flow.StateFlow

interface StateFlowSuspendKrate<T> : SuspendKrate<T> {
    val cachedStateFlow: StateFlow<T>
}
