package ru.astrainteractive.klibs.kstorage.suspend.util

import com.russhwolf.settings.coroutines.FlowSettings
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import ru.astrainteractive.klibs.kstorage.suspend.FlowMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultFlowMutableKrate

internal class DataStoreFlowMutableKrate(
    key: String,
    settings: FlowSettings,
    factory: ValueFactory<Int>,
) : FlowMutableKrate<Int> by DefaultFlowMutableKrate(
    factory = factory,
    loader = { settings.getIntOrNullFlow(key) },
    saver = { value -> settings.putInt(key, value) }
)
