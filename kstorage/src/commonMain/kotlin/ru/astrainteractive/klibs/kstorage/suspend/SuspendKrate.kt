package ru.astrainteractive.klibs.kstorage.suspend

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
