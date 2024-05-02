package ru.astrainteractive.klibs.kstorage.suspend.value

fun interface SuspendValueFactory<T> {
    suspend fun create(): T
}
