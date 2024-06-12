package ru.astrainteractive.klibs.kstorage.suspend.test

import ru.astrainteractive.klibs.kstorage.suspend.SuspendMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultSuspendMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.provider.SuspendValueFactory
import ru.astrainteractive.klibs.kstorage.test.SampleStore

internal class StoreSuspendMutableKrate<T>(
    factory: SuspendValueFactory<T>,
    key: String = "key",
    store: SampleStore = SampleStore()
) : SuspendMutableKrate<T> by DefaultSuspendMutableKrate(
    factory = factory,
    saver = { store.put(key, it) },
    loader = { store.get(key) }
)
