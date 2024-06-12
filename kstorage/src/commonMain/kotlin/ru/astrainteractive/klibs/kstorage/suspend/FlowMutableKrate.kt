package ru.astrainteractive.klibs.kstorage.suspend

interface FlowMutableKrate<T> : FlowKrate<T> {
    suspend fun save(value: T)

    suspend fun reset()
}
