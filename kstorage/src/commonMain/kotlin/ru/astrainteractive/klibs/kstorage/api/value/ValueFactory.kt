package ru.astrainteractive.klibs.kstorage.api.value

fun interface ValueFactory<T> {
    fun create(): T
}
