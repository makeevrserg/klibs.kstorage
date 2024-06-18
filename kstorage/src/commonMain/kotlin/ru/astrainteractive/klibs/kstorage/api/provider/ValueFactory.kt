package ru.astrainteractive.klibs.kstorage.api.provider

fun interface ValueFactory<out T> {
    fun create(): T
}
