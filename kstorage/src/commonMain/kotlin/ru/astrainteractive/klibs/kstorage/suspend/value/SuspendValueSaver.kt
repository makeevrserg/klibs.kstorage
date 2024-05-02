package ru.astrainteractive.klibs.kstorage.suspend.value

fun interface SuspendValueSaver<T> {
    suspend fun save(value: T)
}
