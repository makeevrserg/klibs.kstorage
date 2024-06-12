package ru.astrainteractive.klibs.kstorage.suspend

import kotlinx.coroutines.flow.Flow

interface FlowKrate<T> : SuspendKrate<T> {
    val flow: Flow<T>
}
