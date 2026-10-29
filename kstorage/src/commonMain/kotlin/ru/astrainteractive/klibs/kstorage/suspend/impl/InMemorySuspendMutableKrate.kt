package ru.astrainteractive.klibs.kstorage.suspend.impl

import ru.astrainteractive.klibs.kstorage.lock.api.LockOwner
import ru.astrainteractive.klibs.kstorage.suspend.SuspendMutableKrate

/**
 * Creates [ru.astrainteractive.klibs.kstorage.suspend.SuspendMutableKrate] which value will be stored in-memory
 */
@Suppress("FunctionNaming")
fun <T> InMemorySuspendMutableKrate(factory: () -> T): SuspendMutableKrate<T> {
    var value = factory.invoke()
    return DefaultSuspendMutableKrate(
        factory = { value },
        saver = { newValue -> value = newValue },
        loader = { value },
        lockOwner = LockOwner.Default()
    )
}
