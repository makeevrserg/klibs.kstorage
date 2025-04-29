package ru.astrainteractive.klibs.kstorage.suspend

import kotlinx.coroutines.flow.Flow

/**
 * Represents a mutable Krate that exposes its value as a [Flow] while also supporting
 * asynchronous read and write operations. This interface combines the features of [FlowKrate]
 * and [SuspendMutableKrate], enabling reactive value observation and asynchronous mutation in one interface.
 */
interface FlowMutableKrate<T> : FlowKrate<T>, SuspendMutableKrate<T>
