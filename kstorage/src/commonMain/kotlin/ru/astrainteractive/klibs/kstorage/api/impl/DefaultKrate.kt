package ru.astrainteractive.klibs.kstorage.api.impl

import ru.astrainteractive.klibs.kstorage.api.Krate
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import ru.astrainteractive.klibs.kstorage.api.value.ValueLoader
import ru.astrainteractive.klibs.kstorage.internal.lock.LockOwner

class DefaultKrate<T>(
    private val factory: ValueFactory<T>,
    private val loader: ValueLoader<T>,
) : Krate<T>, LockOwner by LockOwner.Default() {

    override fun getValue(): T {
        return lock.withLock {
            loader.loadAndGet() ?: factory.create()
        }
    }
}
