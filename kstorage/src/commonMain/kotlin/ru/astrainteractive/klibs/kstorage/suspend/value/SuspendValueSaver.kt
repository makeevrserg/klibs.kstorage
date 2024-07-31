package ru.astrainteractive.klibs.kstorage.suspend.value

fun interface SuspendValueSaver<T> {
    suspend fun save(value: T)

    class Empty<T> : SuspendValueSaver<T> {
        override suspend fun save(value: T) = Unit
    }
}
