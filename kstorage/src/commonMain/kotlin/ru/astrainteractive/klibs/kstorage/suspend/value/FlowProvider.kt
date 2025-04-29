package ru.astrainteractive.klibs.kstorage.suspend.value

import kotlinx.coroutines.flow.Flow

/**
 * The provided flow may emit nullable values, allowing consumers to handle the absence of data.
 */
fun interface FlowProvider<out T> {

    /**
     * Provides a [Flow] that asynchronously emits nullable values.
     */
    fun provide(): Flow<T?>
}
