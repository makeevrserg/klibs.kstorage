package ru.astrainteractive.klibs.kstorage.suspend.value

/**
 * This interface provides a method to load a value asynchronously and return it,
 * potentially returning `null` if the value is unavailable.
 */
fun interface SuspendValueLoader<out T> {

    /**
     * Suspends and loads the value asynchronously.
     * @return the loaded value or `null` if the value is not found.
     */
    suspend fun loadAndGet(): T?
}
