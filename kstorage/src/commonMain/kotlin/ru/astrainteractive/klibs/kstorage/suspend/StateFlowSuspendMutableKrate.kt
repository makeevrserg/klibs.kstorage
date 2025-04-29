package ru.astrainteractive.klibs.kstorage.suspend

import kotlinx.coroutines.flow.StateFlow

/**
 * Represents a mutable Krate that exposes its value as a [StateFlow] while also supporting
 * asynchronous read and write operations.
 * This interface combines the features of [StateFlowSuspendKrate] and [SuspendMutableKrate],
 * enabling reactive value observation and asynchronous mutation in one interface.
 */
interface StateFlowSuspendMutableKrate<T> : StateFlowSuspendKrate<T>, SuspendMutableKrate<T>
