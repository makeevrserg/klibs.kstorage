package ru.astrainteractive.klibs.kstorage.suspend

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.StateFlow
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import ru.astrainteractive.klibs.kstorage.coroutines.getIoDispatcher
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultStateFlowSuspendMutableKrate

/**
 * Represents a mutable Krate that exposes its value as a [StateFlow] while also supporting
 * asynchronous read and write operations.
 * This interface combines the features of [StateFlowSuspendKrate] and [SuspendMutableKrate],
 * enabling reactive value observation and asynchronous mutation in one interface.
 */
interface StateFlowSuspendMutableKrate<T> : StateFlowSuspendKrate<T>, SuspendMutableKrate<T>

/**
 * Adds a default value fallback to a nullable StateFlowSuspendMutableKrate, returning a non-nullable variant.
 */
fun <T : Any> StateFlowSuspendMutableKrate<T?>.withDefault(
    factory: ValueFactory<T>,
    coroutineContext: CoroutineDispatcher = getIoDispatcher()
): StateFlowSuspendMutableKrate<T> {
    return DefaultStateFlowSuspendMutableKrate(
        factory = factory,
        loader = { getValue() },
        saver = { value -> save(value) },
        coroutineContext = coroutineContext
    )
}
