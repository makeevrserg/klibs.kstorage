package ru.astrainteractive.klibs.kstorage.api

import ru.astrainteractive.klibs.kstorage.api.impl.DefaultCachedKrate
import ru.astrainteractive.klibs.kstorage.api.impl.DefaultKrate
import ru.astrainteractive.klibs.kstorage.api.impl.DefaultStateFlowKrate
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory

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

/**
 * Converts a nullable Krate into a non-nullable one by supplying a default value via factory.
 */
fun <T : Any> Krate<T?>.withDefault(factory: ValueFactory<T>): Krate<T> {
    return DefaultKrate(
        factory = factory,
        loader = { getValue() }
    )
}

/**
 * Wraps a regular Krate into a CachedKrate to cache its value in memory for faster access.
 */
fun <T> Krate<T>.asCachedKrate(): CachedKrate<T> {
    return DefaultCachedKrate(this)
}

/**
 * Wraps a Krate into a StateFlowKrate, exposing its value as a reactive StateFlow.
 */
fun <T> Krate<T>.asStateFlowKrate(): StateFlowKrate<T> {
    return DefaultStateFlowKrate(this)
}
