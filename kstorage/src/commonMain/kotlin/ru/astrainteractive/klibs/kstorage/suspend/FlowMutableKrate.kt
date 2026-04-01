package ru.astrainteractive.klibs.kstorage.suspend

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultFlowMutableKrate

/**
 * Represents a mutable Krate that exposes its value as a [Flow] while also supporting
 * asynchronous read and write operations. This interface combines the features of [FlowKrate]
 * and [SuspendMutableKrate], enabling reactive value observation and asynchronous mutation in one interface.
 */
interface FlowMutableKrate<T> : FlowKrate<T>, SuspendMutableKrate<T>

/**
 * Converts a nullable FlowMutableKrate to a non-nullable one by using a default factory.
 */
fun <T : Any> FlowMutableKrate<T?>.withDefault(
    factory: ValueFactory<T>,
): FlowMutableKrate<T> {
    return DefaultFlowMutableKrate(
        factory = factory,
        loader = { flow },
        saver = { value -> save(value) },
    )
}

/**
 * Converts this [FlowMutableKrate] into a [StateFlowSuspendMutableKrate],
 * exposing its state as a [StateFlow] that is kept up to date within the given [CoroutineScope].
 */
fun <T> FlowMutableKrate<T>.asStateFlowSuspendMutableKrate(scope: CoroutineScope): StateFlowSuspendMutableKrate<T> {
    val instance = this
    return object : StateFlowSuspendMutableKrate<T> {
        override val cachedStateFlow: StateFlow<T> = instance.stateFlow(scope)

        override suspend fun getValue(): T {
            return instance.getValue()
        }

        override suspend fun save(value: T) {
            instance.save(value)
        }

        override suspend fun save(block: suspend (T) -> T) {
            instance.save(block)
        }

        override suspend fun saveAndGet(block: suspend (T) -> T): T {
            return instance.saveAndGet(block)
        }

        override suspend fun reset() {
            instance.reset()
        }

        override suspend fun resetAndGet(): T {
            return instance.resetAndGet()
        }
    }
}
