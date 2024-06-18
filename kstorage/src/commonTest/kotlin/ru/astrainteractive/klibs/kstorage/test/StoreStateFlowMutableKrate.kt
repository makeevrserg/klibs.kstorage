package ru.astrainteractive.klibs.kstorage.test

import ru.astrainteractive.klibs.kstorage.api.StateFlowMutableKrate
import ru.astrainteractive.klibs.kstorage.api.impl.DefaultStateFlowMutableKrate
import ru.astrainteractive.klibs.kstorage.api.provider.ValueFactory

internal class StoreStateFlowMutableKrate<T>(
    factory: ValueFactory<T>,
    key: String = "key",
    store: SampleStore = SampleStore()
) : StateFlowMutableKrate<T> by DefaultStateFlowMutableKrate<T>(
    factory = factory,
    saver = { store.put(key, it) },
    loader = { store.get(key) }
)
