package ru.astrainteractive.klibs.kstorage.suspend.value

import kotlinx.coroutines.flow.Flow

/**
 * The provided flow may emit nullable values, allowing consumers to handle the absence of data.
 */
public fun interface FlowProvider<out T> {

    /**
     * Provides a [Flow] that asynchronously emits nullable values.
     */
    public fun provide(): Flow<T?>
}
