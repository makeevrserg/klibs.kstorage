package ru.astrainteractive.klibs.kstorage.api.impl

import ru.astrainteractive.klibs.kstorage.api.MutableKrate
import ru.astrainteractive.klibs.kstorage.internal.lock.LockOwner
import ru.astrainteractive.klibs.kstorage.suspend.SuspendMutableKrate

/**
 * Creates [SuspendMutableKrate] which value will be stored in-memory
 */
@Suppress("FunctionNaming")
fun <T> InMemoryMutableKrate(factory: () -> T): MutableKrate<T> {
    var value = factory.invoke()
    return DefaultMutableKrate(
        factory = { value },
        saver = { newValue -> value = newValue },
        loader = { value },
        lockOwner = LockOwner.Default()
    )
}
