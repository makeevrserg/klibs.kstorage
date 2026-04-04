package ru.astrainteractive.klibs.kstorage.suspend

import kotlinx.coroutines.CoroutineDispatcher
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import ru.astrainteractive.klibs.kstorage.coroutines.getIoDispatcher
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultStateFlowSuspendMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultSuspendMutableKrate

/**
 * Represents a mutable Krate that supports asynchronous read and write operations.
 * This interface extends [SuspendKrate], adding the ability to save and reset values
 * in an asynchronous manner, making it suitable for use cases that involve I/O operations.
 */
interface SuspendMutableKrate<T> : SuspendKrate<T> {

    /**
     * Suspends while saving the given value to the underlying storage.
     */
    suspend fun save(value: T)

    /**
     * Suspends while retrieving the current value, transforms it using the suspend block, and saves the result.
     */
    suspend fun save(block: suspend (T) -> T)

    /**
     * Suspends while transforming the current value using the suspend block, saves it, and returns the new value.
     */
    suspend fun saveAndGet(block: suspend (T) -> T): T

    /**
     * Suspends while resetting the stored value to its default or initial state.
     */
    suspend fun reset()

    /**
     * Suspends while resetting the Krate to its default value and returns the new value.
     */
    suspend fun resetAndGet(): T
}

/**
 * Converts a nullable suspend-based MutableKrate into a StateFlowSuspendMutableKrate for reactive value observation.
 */
fun <T : Any> SuspendMutableKrate<T?>.asStateFlowSuspendMutableKrate(
    coroutineContext: CoroutineDispatcher = getIoDispatcher()
): StateFlowSuspendMutableKrate<T?> {
    return DefaultStateFlowSuspendMutableKrate(
        factory = { null },
        loader = { this.getValue() },
        saver = { value -> this.save(value) },
        coroutineContext = coroutineContext
    )
}

/**
 * Converts a nullable SuspendMutableKrate into a non-nullable one by providing a default via the given factory.
 */
fun <T : Any> SuspendMutableKrate<T?>.withDefault(
    factory: ValueFactory<T>,
): SuspendMutableKrate<T> {
    return DefaultSuspendMutableKrate(
        factory = factory,
        loader = { getValue() },
        saver = { value -> save(value) },
    )
}
