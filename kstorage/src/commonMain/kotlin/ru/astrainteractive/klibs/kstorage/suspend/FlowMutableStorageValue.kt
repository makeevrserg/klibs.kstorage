package ru.astrainteractive.klibs.kstorage.suspend

interface FlowMutableStorageValue<T> : FlowStorageValue<T> {
    suspend fun save(value: T)

    suspend fun reset()
}
