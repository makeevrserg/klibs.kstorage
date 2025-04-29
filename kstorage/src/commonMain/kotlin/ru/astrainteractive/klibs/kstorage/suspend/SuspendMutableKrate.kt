package ru.astrainteractive.klibs.kstorage.suspend

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
     * Suspends while resetting the stored value to its default or initial state.
     */
    suspend fun reset()
}
