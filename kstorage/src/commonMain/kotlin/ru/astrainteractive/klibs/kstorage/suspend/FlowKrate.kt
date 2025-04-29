package ru.astrainteractive.klibs.kstorage.suspend

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow

/**
 * A [Krate] that exposes its value as a Kotlin [Flow] for reactive consumption.
 * Inherits from [SuspendKrate], so it supports suspend-based value loading.
 */
interface FlowKrate<T> : SuspendKrate<T> {
    /**
     * A cold [Flow] that emits values when they change.
     * Consumers can collect this to observe value updates over time.
     */
    val flow: Flow<T>

    /**
     * Converts the underlying [Flow] into a [StateFlow] that maintains a current value.
     *
     * @param coroutineScope The scope in which the StateFlow will be active.
     * @param sharingStarted Defines when the StateFlow starts/stops sharing values.
     * @param dispatcher The coroutine dispatcher used for state emission.
     * @return A [StateFlow] instance reflecting the current Krate value.
     */
    fun stateFlow(
        coroutineScope: CoroutineScope,
        sharingStarted: SharingStarted = SharingStarted.Eagerly,
        dispatcher: CoroutineDispatcher = Dispatchers.Unconfined
    ): StateFlow<T>

    /**
     * Extension property to easily obtain a [StateFlow] from this [FlowKrate]
     * within the current [CoroutineScope].
     */
    val CoroutineScope.stateFlow: StateFlow<T>
        get() = stateFlow(coroutineScope = this)
}
