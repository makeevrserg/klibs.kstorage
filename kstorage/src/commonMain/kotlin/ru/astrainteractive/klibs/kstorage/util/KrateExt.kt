package ru.astrainteractive.klibs.kstorage.util

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
import kotlin.reflect.KProperty

/**
 * This will call [MutableKrate] and return [CacheOwner.cachedValue]
 */
fun <T> MutableKrate<T>.resetAndGet(): T {
    reset()
    return getValue()
}

/**
 * Save value with a reference to current
 */
fun <T> MutableKrate<T>.update(block: (T) -> T) {
    val oldValue = getValue()
    val newValue = block.invoke(oldValue)
    save(newValue)
}

/**
 * Save value with a reference to current and return new value
 */
fun <T> MutableKrate<T>.updateAndGet(block: (T) -> T): T {
    val oldValue = getValue()
    val newValue = block.invoke(oldValue)
    save(newValue)
    return newValue
}

/**
 * convert nullable [Krate] to type-safe via decorating [DefaultMutableKrate]
 */
fun <T : Any> Krate<T?>.withDefault(factory: ValueFactory<T>): Krate<T> {
    return DefaultMutableKrate(
        factory = factory,
        loader = { getValue() }
    )
}

/**
 * convert nullable [ Krate.Mutable] to type-safe via decorating [DefaultMutableKrate]
 */
fun <T : Any> MutableKrate<T?>.withDefault(factory: ValueFactory<T>): MutableKrate<T> {
    return DefaultMutableKrate(
        factory = factory,
        saver = { value -> save(value) },
        loader = { getValue() }
    )
}

operator fun <T> CachedKrate<T>.getValue(thisRef: Any, property: KProperty<*>): T {
    return this.cachedValue
}

fun <T> Krate<T>.asCachedKrate(): CachedKrate<T> {
    return DefaultCachedKrate(this)
}

fun <T> MutableKrate<T>.asCachedMutableKrate(): CachedMutableKrate<T> {
    return DefaultCachedMutableKrate(this)
}

fun <T> Krate<T>.asStateFlowKrate(): StateFlowKrate<T> {
    return DefaultStateFlowKrate(this)
}

fun <T> MutableKrate<T>.asStateFlowMutableKrate(): StateFlowMutableKrate<T> {
    return DefaultStateFlowMutableKrate(this)
}
