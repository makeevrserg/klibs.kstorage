package ru.astrainteractive.klibs.kstorage.suspend.test

import ru.astrainteractive.klibs.kstorage.api.provider.ValueFactory
import ru.astrainteractive.klibs.kstorage.suspend.SuspendKrate
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultSuspendMutableKrate
import ru.astrainteractive.klibs.kstorage.test.SampleStore

internal class StoreSuspendMutableKrate<T>(
    factory: ValueFactory<T>,
    key: String = "key",
    store: SampleStore = SampleStore()
) : SuspendKrate.Mutable<T> by DefaultSuspendMutableKrate(
    factory = factory,
    saver = { store.put(key, it) },
    loader = { store.get(key) }
)
