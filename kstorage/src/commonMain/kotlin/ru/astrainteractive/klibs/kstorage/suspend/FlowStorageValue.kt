package ru.astrainteractive.klibs.kstorage.suspend

import kotlinx.coroutines.flow.Flow

interface FlowStorageValue<T> {
    val flow: Flow<T>

    suspend fun load(): T
}
