package ru.astrainteractive.klibs.kstorage.test

import ru.astrainteractive.klibs.kstorage.api.MutableKrate
import ru.astrainteractive.klibs.kstorage.api.impl.DefaultMutableKrate
import ru.astrainteractive.klibs.kstorage.api.provider.ValueFactory

internal class StoreMutableKrate<T>(
    factory: ValueFactory<T>,
    key: String = "key",
    store: SampleStore = SampleStore()
) : MutableKrate<T> by DefaultMutableKrate<T>(
    factory = factory,
    saver = { store.put(key, it) },
    loader = { store.get(key) }
)
