package ru.astrainteractive.klibs.kstorage.suspend

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.StateFlow
import ru.astrainteractive.klibs.kstorage.coroutines.getIoDispatcher
import ru.astrainteractive.klibs.kstorage.lock.api.LockOwner
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultStateFlowSuspendMutableKrate
import kotlin.reflect.KProperty

/**
 * Represents a Krate that exposes its value as a [StateFlow] while supporting asynchronous
 * value retrieval. This interface is ideal for cases where you need to both observe state
 * changes reactively and load the value asynchronously.
 */
public interface StateFlowSuspendKrate<T> : SuspendKrate<T> {

    /**
     * A [StateFlow] that holds the current value, allowing consumers to reactively observe
     * state changes over time.
     */
    public val cachedStateFlow: StateFlow<T>
}

/**
 * Adds a default value fallback to a nullable StateFlowSuspendKrate, producing a non-nullable variant.
 */
public fun <T : Any> StateFlowSuspendKrate<T?>.withDefault(
    factory: () -> T,
    coroutineContext: CoroutineDispatcher = getIoDispatcher()
): StateFlowSuspendKrate<T> {
    return DefaultStateFlowSuspendMutableKrate(
        factory = factory,
        loader = { getValue() },
        coroutineContext = coroutineContext,
        lockOwner = LockOwner.Reusable(this)
    )
}

/**
 * Enables Kotlin property delegation to access the current value of a StateFlowSuspendKrate.
 */
public operator fun <T> StateFlowSuspendKrate<T>.getValue(thisRef: Any, property: KProperty<*>): T {
    return this.cachedStateFlow.value
}

/**
 * Enables Kotlin property delegation to access the current value of a StateFlowSuspendKrate.
 */
public operator fun <T> StateFlowSuspendKrate<T>.getValue(thisRef: Nothing?, property: KProperty<*>): T {
    return this.cachedStateFlow.value
}
