@file:Suppress("TooManyFunctions")

package ru.astrainteractive.klibs.kstorage.util

import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import ru.astrainteractive.klibs.kstorage.coroutines.getIoDispatcher
import ru.astrainteractive.klibs.kstorage.suspend.FlowKrate
import ru.astrainteractive.klibs.kstorage.suspend.FlowMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.StateFlowSuspendKrate
import ru.astrainteractive.klibs.kstorage.suspend.StateFlowSuspendMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.SuspendKrate
import ru.astrainteractive.klibs.kstorage.suspend.SuspendMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultFlowMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultStateFlowSuspendMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultSuspendMutableKrate
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KProperty

/**
 * Suspends while resetting the Krate to its default value and returns the new value.
 */
suspend fun <T> SuspendMutableKrate<T>.resetAndGet(): T {
    reset()
    return getValue()
}

/**
 * Suspends while retrieving the current value, transforms it using the suspend block, and saves the result.
 */
suspend fun <T> SuspendMutableKrate<T>.save(block: suspend (T) -> T) {
    val oldValue = getValue()
    val newValue = block.invoke(oldValue)
    save(newValue)
}

/**
 * Suspends while transforming the current value using the suspend block, saves it, and returns the new value.
 */
suspend fun <T> SuspendMutableKrate<T>.saveAndGet(block: suspend (T) -> T): T {
    val oldValue = getValue()
    val newValue = block.invoke(oldValue)
    save(newValue)
    return newValue
}

/**
 * Converts a nullable SuspendKrate into a non-nullable one by using a fallback factory when null is encountered.
 */
fun <T : Any> SuspendKrate<T?>.withDefault(
    factory: ValueFactory<T>,
): SuspendKrate<T> {
    return DefaultSuspendMutableKrate(
        factory = factory,
        loader = { getValue() },
    )
}

/**
 * Converts a nullable SuspendMutableKrate into a non-nullable one by providing a default via the given factory.
 */
fun <T : Any> SuspendMutableKrate<T?>.withDefault(
    factory: ValueFactory<T>,
): SuspendMutableKrate<T> {
    return DefaultSuspendMutableKrate(
        factory = factory,
        loader = { getValue() },
        saver = { value -> save(value) },
    )
}

/**
 * Adds a default value fallback to a nullable StateFlowSuspendKrate, producing a non-nullable variant.
 */
fun <T : Any> StateFlowSuspendKrate<T?>.withDefault(
    factory: ValueFactory<T>,
    coroutineContext: CoroutineContext = getIoDispatcher()
): StateFlowSuspendKrate<T> {
    return DefaultStateFlowSuspendMutableKrate(
        factory = factory,
        loader = { getValue() },
        coroutineContext = coroutineContext
    )
}

/**
 * Adds a default value fallback to a nullable StateFlowSuspendMutableKrate, returning a non-nullable variant.
 */
fun <T : Any> StateFlowSuspendMutableKrate<T?>.withDefault(
    factory: ValueFactory<T>,
    coroutineContext: CoroutineContext = getIoDispatcher()
): StateFlowSuspendMutableKrate<T> {
    return DefaultStateFlowSuspendMutableKrate(
        factory = factory,
        loader = { getValue() },
        saver = { value -> save(value) },
        coroutineContext = coroutineContext
    )
}

/**
 * Converts a nullable FlowKrate to a non-nullable one by applying a default factory.
 */
fun <T : Any> FlowKrate<T?>.withDefault(
    factory: ValueFactory<T>,
): FlowKrate<T> {
    return DefaultFlowMutableKrate(
        factory = factory,
        loader = { flow },
    )
}

/**
 * Converts a nullable FlowMutableKrate to a non-nullable one by using a default factory.
 */
fun <T : Any> FlowMutableKrate<T?>.withDefault(
    factory: ValueFactory<T>,
): FlowMutableKrate<T> {
    return DefaultFlowMutableKrate(
        factory = factory,
        loader = { flow },
        saver = { value -> save(value) },
    )
}

/**
 * Enables Kotlin property delegation to access the current value of a StateFlowSuspendKrate.
 */
operator fun <T> StateFlowSuspendKrate<T>.getValue(thisRef: Any, property: KProperty<*>): T {
    return this.cachedStateFlow.value
}

/**
 * Enables Kotlin property delegation to access the current value of a StateFlowSuspendKrate.
 */
operator fun <T> StateFlowSuspendKrate<T>.getValue(thisRef: Nothing?, property: KProperty<*>): T {
    return this.cachedStateFlow.value
}

/**
 * Converts a nullable suspend-based MutableKrate into a StateFlowSuspendMutableKrate for reactive value observation.
 */
fun <T : Any> SuspendMutableKrate<T?>.asStateFlowMutableKrate(
    coroutineContext: CoroutineContext = getIoDispatcher()
): StateFlowSuspendMutableKrate<T?> {
    return DefaultStateFlowSuspendMutableKrate(
        factory = { null },
        loader = { this.getValue() },
        saver = { value -> this.save(value) },
        coroutineContext = coroutineContext
    )
}
