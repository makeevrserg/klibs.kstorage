package ru.astrainteractive.klibs.kstorage.suspend

import kotlinx.coroutines.CoroutineDispatcher
import ru.astrainteractive.klibs.kstorage.coroutines.getIoDispatcher
import ru.astrainteractive.klibs.kstorage.lock.api.LockOwner
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultStateFlowSuspendMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultSuspendMutableKrate

/**
 * Represents a mutable Krate that supports asynchronous read and write operations.
 * This interface extends [SuspendKrate], adding the ability to save and reset values
 * in an asynchronous manner, making it suitable for use cases that involve I/O operations.
 */
public interface SuspendMutableKrate<T> : SuspendKrate<T> {

    /**
     * Suspends while saving the given value to the underlying storage.
     */
    public suspend fun save(value: T)

    /**
     * Suspends while retrieving the current value, transforms it using the suspend block, and saves the result.
     */
    public suspend fun save(block: suspend (T) -> T)

    /**
     * Suspends while transforming the current value using the suspend block, saves it, and returns the new value.
     */
    public suspend fun saveAndGet(block: suspend (T) -> T): T

    /**
     * Suspends while resetting the stored value to its default or initial state.
     */
    public suspend fun reset()

    /**
     * Suspends while resetting the Krate to its default value and returns the new value.
     */
    public suspend fun resetAndGet(): T
}

/**
 * Converts a nullable suspend-based MutableKrate into a StateFlowSuspendMutableKrate for reactive value observation.
 */
public fun <T : Any> SuspendMutableKrate<T?>.asStateFlowSuspendMutableKrate(
    coroutineContext: CoroutineDispatcher = getIoDispatcher()
): StateFlowSuspendMutableKrate<T?> {
    return DefaultStateFlowSuspendMutableKrate(
        factory = { null },
        loader = { this.getValue() },
        saver = { value -> this.save(value) },
        coroutineContext = coroutineContext,
        lockOwner = LockOwner.Reusable(this)
    )
}

/**
 * Converts a nullable SuspendMutableKrate into a non-nullable one by providing a default via the given factory.
 */
public fun <T : Any> SuspendMutableKrate<T?>.withDefault(
    factory: () -> T,
): SuspendMutableKrate<T> {
    return DefaultSuspendMutableKrate(
        factory = factory,
        loader = { getValue() },
        saver = { value -> save(value) },
        lockOwner = LockOwner.Reusable(this)
    )
}
