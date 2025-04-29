package ru.astrainteractive.klibs.kstorage.suspend

import kotlinx.coroutines.flow.StateFlow

/**
 * Represents a Krate that exposes its value as a [StateFlow] while supporting asynchronous
 * value retrieval. This interface is ideal for cases where you need to both observe state
 * changes reactively and load the value asynchronously.
 */
interface StateFlowSuspendKrate<T> : SuspendKrate<T> {

    /**
     * A [StateFlow] that holds the current value, allowing consumers to reactively observe
     * state changes over time.
     */
    val cachedStateFlow: StateFlow<T>
}
