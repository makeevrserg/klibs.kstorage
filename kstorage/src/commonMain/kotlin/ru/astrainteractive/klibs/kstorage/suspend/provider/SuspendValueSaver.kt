package ru.astrainteractive.klibs.kstorage.suspend.provider

fun interface SuspendValueSaver<T> {
    suspend fun save(value: T)

    class Empty<T> : SuspendValueSaver<T> {
        override suspend fun save(value: T) = Unit
    }
}
