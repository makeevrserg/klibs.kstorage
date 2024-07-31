package ru.astrainteractive.klibs.kstorage.suspend

import ru.astrainteractive.klibs.kstorage.api.cache.CacheOwner

interface SuspendKrate<T> : CacheOwner<T> {
    suspend fun loadAndGet(): T
}
