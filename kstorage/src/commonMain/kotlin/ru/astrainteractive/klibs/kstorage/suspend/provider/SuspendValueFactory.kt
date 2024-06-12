package ru.astrainteractive.klibs.kstorage.suspend.provider

fun interface SuspendValueFactory<T> {
    suspend fun create(): T
}
