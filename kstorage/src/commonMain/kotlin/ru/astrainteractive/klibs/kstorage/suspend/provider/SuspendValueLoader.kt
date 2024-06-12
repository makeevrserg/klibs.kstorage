package ru.astrainteractive.klibs.kstorage.suspend.provider

fun interface SuspendValueLoader<out T> {
    suspend fun loadAndGet(): T?
}
