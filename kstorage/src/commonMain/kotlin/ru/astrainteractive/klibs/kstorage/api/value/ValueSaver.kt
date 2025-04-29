package ru.astrainteractive.klibs.kstorage.api.value

/**
 * This interface provides a method to save a value synchronously.
 */
fun interface ValueSaver<T> {

    /**
     * Saves the provided value synchronously.
     */
    fun save(value: T)

    /**
     * A no-op implementation of [ValueSaver], which ignores any values passed to `save`.
     */
    class Empty<T> : ValueSaver<T> {
        override fun save(value: T) = Unit
    }
}
