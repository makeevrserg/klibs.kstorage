package ru.astrainteractive.klibs.kstorage.api.value

fun interface ValueLoader<out T> {
    fun loadAndGet(): T?
}
