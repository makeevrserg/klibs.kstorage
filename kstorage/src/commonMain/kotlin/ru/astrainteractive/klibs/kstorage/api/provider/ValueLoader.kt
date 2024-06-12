package ru.astrainteractive.klibs.kstorage.api.provider

fun interface ValueLoader<out T> {
    fun loadAndGet(): T?
}
