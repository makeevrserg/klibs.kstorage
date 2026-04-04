package ru.astrainteractive.klibs.kstorage.suspend

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import ru.astrainteractive.klibs.kstorage.coroutines.getIoDispatcher
import ru.astrainteractive.klibs.kstorage.internal.lock.LockOwner
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultFlowMutableKrate

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
     * @param coroutineDispatcher The coroutine dispatcher used for state emission.
     * @return A [StateFlow] instance reflecting the current Krate value.
     */
    fun stateFlow(
        coroutineScope: CoroutineScope,
        sharingStarted: SharingStarted = SharingStarted.Eagerly,
        coroutineDispatcher: CoroutineDispatcher = getIoDispatcher()
    ): StateFlow<T>

    /**
     * Extension property to easily obtain a [StateFlow] from this [FlowKrate]
     * within the current [CoroutineScope].
     */
    val CoroutineScope.stateFlow: StateFlow<T>
        get() = stateFlow(coroutineScope = this)
}

/**
 * Converts a nullable FlowKrate to a non-nullable one by applying a default factory.
 */
fun <T : Any> FlowKrate<T?>.withDefault(
    factory: ValueFactory<T>,
): FlowKrate<T> {
    return DefaultFlowMutableKrate(
        factory = factory,
        loader = { flow },
        lockOwner = LockOwner.Reusable(this)
    )
}
