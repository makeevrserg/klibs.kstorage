package ru.astrainteractive.klibs.kstorage.api

import kotlinx.coroutines.flow.StateFlow

/**
 * Represents a mutable Krate that combines caching, state flow, and modification capabilities.
 * This interface allows access to a cached value, provides a reactive [StateFlow] for state observation,
 * and supports saving and resetting the stored value.
 */
interface StateFlowMutableKrate<T> : CachedMutableKrate<T>, StateFlowKrate<T>
