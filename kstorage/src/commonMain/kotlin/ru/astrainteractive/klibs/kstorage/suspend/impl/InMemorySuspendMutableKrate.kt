package ru.astrainteractive.klibs.kstorage.suspend.impl

import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import ru.astrainteractive.klibs.kstorage.internal.lock.LockOwner
import ru.astrainteractive.klibs.kstorage.suspend.SuspendMutableKrate

/**
 * Creates [ru.astrainteractive.klibs.kstorage.suspend.SuspendMutableKrate] which value will be stored in-memory
 */
@Suppress("FunctionNaming")
fun <T> InMemorySuspendMutableKrate(factory: ValueFactory<T>): SuspendMutableKrate<T> {
    var value = factory.create()
    return DefaultSuspendMutableKrate(
        factory = { value },
        saver = { newValue -> value = newValue },
        loader = { value },
        lockOwner = LockOwner.Default()
    )
}
