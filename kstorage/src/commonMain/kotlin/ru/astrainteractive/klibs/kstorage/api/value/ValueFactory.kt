package ru.astrainteractive.klibs.kstorage.api.value

/**
 * This interface provides a method to create a new instance of the desired value.
 */
fun interface ValueFactory<out T> {

    /**
     * Creates and returns a new instance of the value.
     */
    fun create(): T
}
