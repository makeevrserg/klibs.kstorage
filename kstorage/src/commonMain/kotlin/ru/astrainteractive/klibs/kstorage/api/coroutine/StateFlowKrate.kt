package ru.astrainteractive.klibs.kstorage.api.coroutine

import ru.astrainteractive.klibs.kstorage.api.Krate
import ru.astrainteractive.klibs.kstorage.api.cache.CoroutineCacheOwner

interface StateFlowKrate<T> : Krate<T>, CoroutineCacheOwner<T>
