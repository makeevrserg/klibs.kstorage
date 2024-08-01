package ru.astrainteractive.klibs.kstorage.api.flow

import ru.astrainteractive.klibs.kstorage.api.Krate
import ru.astrainteractive.klibs.kstorage.api.cache.StateFlowCacheOwner

interface StateFlowKrate<T> : Krate<T>, StateFlowCacheOwner<T>
