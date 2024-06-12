package ru.astrainteractive.klibs.kstorage.suspend

interface SuspendKrate<T> {
    suspend fun getValue(): T
}
