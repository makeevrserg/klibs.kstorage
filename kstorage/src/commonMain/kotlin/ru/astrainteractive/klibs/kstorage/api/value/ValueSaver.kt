package ru.astrainteractive.klibs.kstorage.api.value

/**
 * This interface provides a method to save a value synchronously.
 */
public fun interface ValueSaver<T> {

    /**
     * Saves the provided value synchronously.
     */
    public fun save(value: T)

    /**
     * A no-op implementation of [ValueSaver], which ignores any values passed to `save`.
     */
    public class Empty<T> : ValueSaver<T> {
        override fun save(value: T): Unit = Unit
    }
}
