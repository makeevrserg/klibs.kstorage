package ru.astrainteractive.klibs.kstorage.suspend

interface SuspendMutableKrate<T> : SuspendKrate<T> {
    suspend fun save(value: T)

    suspend fun reset()
}
