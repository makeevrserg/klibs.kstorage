package ru.astrainteractive.klibs.kstorage.api.impl

import ru.astrainteractive.klibs.kstorage.api.MutableKrate
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import ru.astrainteractive.klibs.kstorage.suspend.SuspendMutableKrate

/**
 * Creates [SuspendMutableKrate] which value will be stored in-memory
 */
@Suppress("FunctionNaming")
fun <T> InMemoryMutableKrate(factory: ValueFactory<T>): MutableKrate<T> {
    var value = factory.create()
    return DefaultMutableKrate(
        factory = { value },
        saver = { newValue -> value = newValue },
        loader = { value }
    )
}
