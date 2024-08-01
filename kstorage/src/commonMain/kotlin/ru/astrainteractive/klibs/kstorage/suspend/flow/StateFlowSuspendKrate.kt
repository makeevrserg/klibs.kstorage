package ru.astrainteractive.klibs.kstorage.suspend.flow

import ru.astrainteractive.klibs.kstorage.api.cache.StateFlowCacheOwner
import ru.astrainteractive.klibs.kstorage.suspend.SuspendKrate

interface StateFlowSuspendKrate<T> : SuspendKrate<T>, StateFlowCacheOwner<T>
