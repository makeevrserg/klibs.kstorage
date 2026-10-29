package ru.astrainteractive.klibs.kstorage.api

import kotlinx.coroutines.flow.StateFlow

/**
 * Represents a Krate that exposes its cached value as a [StateFlow].
 * This interface combines the ability to access a cached value with reactive state management,
 * allowing consumers to observe changes to the value over time.
 */
public interface StateFlowKrate<T> : CachedKrate<T> {

    /**
     * A [StateFlow] that holds the current cached value and allows reactive observation of state changes.
     */
    public val cachedStateFlow: StateFlow<T>
}
