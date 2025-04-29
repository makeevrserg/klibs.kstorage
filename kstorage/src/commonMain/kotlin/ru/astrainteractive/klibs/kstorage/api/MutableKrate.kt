package ru.astrainteractive.klibs.kstorage.api

/**
 * Represents a mutable key-value container (Krate) that allows both reading and modifying its stored value.
 * This interface provides the ability to save a new value and reset the value to its initial state.
 */
interface MutableKrate<T> : Krate<T> {

    /**
     * Saves the provided value
     */
    fun save(value: T)

    /**
     * Resets the stored value to its default or initial state.
     */
    fun reset()
}
