package ru.astrainteractive.klibs.kstorage.suspend

import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultSuspendMutableKrate

/**
 * Represents a suspendable read-only key-value container (Krate).
 * The value can be loaded asynchronously, making it suitable for I/O-bound sources
 * like databases, files, or network-backed storages.
 */
interface SuspendKrate<T> {
    /**
     * Suspends while retrieving the current value from the underlying storage or cache.
     */
    suspend fun getValue(): T
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
