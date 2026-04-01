package ru.astrainteractive.klibs.kstorage.api

import ru.astrainteractive.klibs.kstorage.api.impl.DefaultCachedMutableKrate
import ru.astrainteractive.klibs.kstorage.api.impl.DefaultMutableKrate
import ru.astrainteractive.klibs.kstorage.api.impl.DefaultStateFlowMutableKrate
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory

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
     * Applies a transformation to the current value using the provided block and saves the result.
     */
    fun save(block: (T) -> T)

    /**
     * Applies a transformation to the current value, saves the result, and returns the updated value.
     */
    fun saveAndGet(block: (T) -> T): T

    /**
     * Resets the stored value to its default or initial state.
     */
    fun reset()

    /**
     * Resets the Krate value to its default (via factory) and returns the new value.
     */
    fun resetAndGet(): T
}

/**
 * Converts a nullable MutableKrate into a non-nullable one using a fallback factory.
 */
fun <T : Any> MutableKrate<T?>.withDefault(factory: ValueFactory<T>): MutableKrate<T> {
    return DefaultMutableKrate(
        factory = factory,
        saver = { value -> save(value) },
        loader = { getValue() }
    )
}

/**
 * Wraps a MutableKrate into a CachedMutableKrate to allow mutation and in-memory caching.
 */
fun <T> MutableKrate<T>.asCachedMutableKrate(): CachedMutableKrate<T> {
    return DefaultCachedMutableKrate(this)
}

/**
 * Wraps a MutableKrate into a StateFlowMutableKrate to support reactive value observation and mutation.
 */
fun <T> MutableKrate<T>.asStateFlowMutableKrate(): StateFlowMutableKrate<T> {
    return DefaultStateFlowMutableKrate(this)
}
