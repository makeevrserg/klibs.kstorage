package ru.astrainteractive.klibs.kstorage.api.value

/**
 * This interface provides a method to load and return a value,
 * potentially returning `null` if the value is not available.
 */
fun interface ValueLoader<out T> {

    /**
     * Loads and returns a value, or `null` if the value is not found.
     */
    fun loadAndGet(): T?
}
