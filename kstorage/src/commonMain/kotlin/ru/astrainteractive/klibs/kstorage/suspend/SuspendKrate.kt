package ru.astrainteractive.klibs.kstorage.suspend

import ru.astrainteractive.klibs.kstorage.api.CachedKrate

interface SuspendKrate<T> : CachedKrate<T> {
    suspend fun loadAndGet(): T
}
