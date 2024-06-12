package ru.astrainteractive.klibs.kstorage.suspend

import kotlinx.coroutines.flow.Flow

interface FlowKrate<T> {
    val flow: Flow<T>

    suspend fun loadAndGet(): T
}
