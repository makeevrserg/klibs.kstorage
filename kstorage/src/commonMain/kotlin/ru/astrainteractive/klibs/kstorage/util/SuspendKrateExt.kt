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
 * This will call [SuspendMutableKrate.reset] and return first [SuspendKrate.getValue]
 */
suspend fun <T> SuspendMutableKrate<T>.resetAndGet(): T {
    reset()
    return getValue()
}

/**
 * Save value with a reference to current
 */
suspend fun <T> SuspendMutableKrate<T>.update(block: suspend (T) -> T) {
    val oldValue = getValue()
    val newValue = block.invoke(oldValue)
    save(newValue)
}

/**
 * Save value with a reference to current and return new value
 */
suspend fun <T> SuspendMutableKrate<T>.updateAndGet(block: suspend (T) -> T): T {
    val oldValue = getValue()
    val newValue = block.invoke(oldValue)
    save(newValue)
    return newValue
}

/**
 * convert nullable [FlowKrate] to type-safe via decorating [DefaultSuspendMutableKrate]
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
 * convert nullable [SuspendMutableKrate] to type-safe via decorating [DefaultSuspendMutableKrate]
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
 * convert nullable [StateFlowSuspendMutableKrate] to type-safe via decorating [DefaultSuspendMutableKrate]
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
 * convert nullable [StateFlowSuspendMutableKrate] to type-safe via decorating [DefaultSuspendMutableKrate]
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
 * convert nullable [FlowKrate] to type-safe via decorating [DefaultFlowMutableKrate]
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
 * convert nullable [FlowMutableKrate] to type-safe via decorating [DefaultFlowMutableKrate]
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

operator fun <T> StateFlowSuspendKrate<T>.getValue(thisRef: Any, property: KProperty<*>): T {
    return this.cachedStateFlow.value
}

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
