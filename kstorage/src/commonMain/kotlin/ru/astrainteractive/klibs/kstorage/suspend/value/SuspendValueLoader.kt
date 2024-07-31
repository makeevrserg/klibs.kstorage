package ru.astrainteractive.klibs.kstorage.suspend.value

fun interface SuspendValueLoader<out T> {
    suspend fun loadAndGet(): T?
}
