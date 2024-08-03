package ru.astrainteractive.klibs.kstorage.api.value

fun interface ValueFactory<out T> {
    fun create(): T
}
