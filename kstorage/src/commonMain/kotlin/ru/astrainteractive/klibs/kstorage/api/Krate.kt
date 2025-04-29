package ru.astrainteractive.klibs.kstorage.api

/**
 * Represents a key-value container (Krate) that provides synchronous access to its stored value.
 * This interface is suitable for cases where the value is available immediately and doesn't require
 * asynchronous loading.
 */
interface Krate<T> {

    /**
     * Returns the current value
     */
    fun getValue(): T
}
