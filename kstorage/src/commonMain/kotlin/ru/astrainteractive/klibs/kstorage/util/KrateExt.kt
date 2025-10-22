@file:Suppress("TooManyFunctions")

package ru.astrainteractive.klibs.kstorage.util

import kotlinx.coroutines.flow.MutableStateFlow
import ru.astrainteractive.klibs.kstorage.api.CachedKrate
import ru.astrainteractive.klibs.kstorage.api.CachedMutableKrate
import ru.astrainteractive.klibs.kstorage.api.Krate
import ru.astrainteractive.klibs.kstorage.api.MutableKrate
import ru.astrainteractive.klibs.kstorage.api.StateFlowKrate
import ru.astrainteractive.klibs.kstorage.api.StateFlowMutableKrate
import ru.astrainteractive.klibs.kstorage.api.impl.DefaultCachedKrate
import ru.astrainteractive.klibs.kstorage.api.impl.DefaultCachedMutableKrate
import ru.astrainteractive.klibs.kstorage.api.impl.DefaultMutableKrate
import ru.astrainteractive.klibs.kstorage.api.impl.DefaultStateFlowKrate
import ru.astrainteractive.klibs.kstorage.api.impl.DefaultStateFlowMutableKrate
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import ru.astrainteractive.klibs.kstorage.suspend.SuspendMutableKrate
import kotlin.reflect.KProperty

/**
 * Resets the Krate value to its default (via factory) and returns the new value.
 */
fun <T> MutableKrate<T>.resetAndGet(): T {
    reset()
    return getValue()
}

/**
 * Applies a transformation to the current value using the provided block and saves the result.
 */
fun <T> MutableKrate<T>.save(block: (T) -> T) {
    val oldValue = getValue()
    val newValue = block.invoke(oldValue)
    save(newValue)
}

/**
 * Applies a transformation to the current value, saves the result, and returns the updated value.
 */
fun <T> MutableKrate<T>.saveAndGet(block: (T) -> T): T {
    val oldValue = getValue()
    val newValue = block.invoke(oldValue)
    save(newValue)
    return newValue
}

/**
 * Converts a nullable Krate into a non-nullable one by supplying a default value via factory.
 */
fun <T : Any> Krate<T?>.withDefault(factory: ValueFactory<T>): Krate<T> {
    return DefaultMutableKrate(
        factory = factory,
        loader = { getValue() }
    )
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
 * Allows Kotlin's property delegation syntax to access the cached value of a CachedKrate.
 */
operator fun <T> CachedKrate<T>.getValue(thisRef: Any, property: KProperty<*>): T {
    return this.cachedValue
}

/**
 * Allows Kotlin's property delegation syntax to access the cached value of a CachedKrate.
 */
operator fun <T> CachedKrate<T>.getValue(thisRef: Nothing?, property: KProperty<*>): T {
    return this.getValue()
}

/**
 * Wraps a regular Krate into a CachedKrate to cache its value in memory for faster access.
 */
fun <T> Krate<T>.asCachedKrate(): CachedKrate<T> {
    return DefaultCachedKrate(this)
}

/**
 * Wraps a MutableKrate into a CachedMutableKrate to allow mutation and in-memory caching.
 */
fun <T> MutableKrate<T>.asCachedMutableKrate(): CachedMutableKrate<T> {
    return DefaultCachedMutableKrate(this)
}

/**
 * Wraps a Krate into a StateFlowKrate, exposing its value as a reactive StateFlow.
 */
fun <T> Krate<T>.asStateFlowKrate(): StateFlowKrate<T> {
    return DefaultStateFlowKrate(this)
}

/**
 * Wraps a MutableKrate into a StateFlowMutableKrate to support reactive value observation and mutation.
 */
fun <T> MutableKrate<T>.asStateFlowMutableKrate(): StateFlowMutableKrate<T> {
    return DefaultStateFlowMutableKrate(this)
}

/**
 * Creates [SuspendMutableKrate] which value will be stored in-memory
 */
@Suppress("FunctionNaming")
fun <T> InMemoryMutableKrate(factory: ValueFactory<T>): MutableKrate<T> {
    val stateFlowValue by lazy { MutableStateFlow(factory.create()) }
    return DefaultMutableKrate(
        factory = factory,
        saver = { newValue -> stateFlowValue.value = newValue },
        loader = { stateFlowValue.value }
    )
}
