package ru.astrainteractive.klibs.kstorage.suspend.value

/**
 * This interface allows saving a value asynchronously with a single `save` function.
 */
public fun interface SuspendValueSaver<T> {

    /**
     * Suspends and saves the provided value asynchronously.
     */
    public suspend fun save(value: T)

    /**
     * A no-op implementation of [SuspendValueSaver], which ignores any values passed to `save`.
     */
    public class Empty<T> : SuspendValueSaver<T> {
        override suspend fun save(value: T): Unit = Unit
    }
}
