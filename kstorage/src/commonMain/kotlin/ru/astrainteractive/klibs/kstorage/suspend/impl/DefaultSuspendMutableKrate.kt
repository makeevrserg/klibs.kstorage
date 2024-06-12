package ru.astrainteractive.klibs.kstorage.suspend.impl

import ru.astrainteractive.klibs.kstorage.suspend.SuspendMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.provider.SuspendValueFactory
import ru.astrainteractive.klibs.kstorage.suspend.provider.SuspendValueLoader
import ru.astrainteractive.klibs.kstorage.suspend.provider.SuspendValueSaver

class DefaultSuspendMutableKrate<T>(
    private val factory: SuspendValueFactory<T>,
    private val loader: SuspendValueLoader<T>,
    private val saver: SuspendValueSaver<T> = SuspendValueSaver.Empty()
) : SuspendMutableKrate<T> {

    override suspend fun getValue(): T {
        return loader.loadAndGet() ?: factory.create()
    }

    override suspend fun save(value: T) {
        if (saver is SuspendValueSaver.Empty) return
        saver.save(value)
    }

    override suspend fun reset() {
        val default = factory.create()
        save(default)
    }
}
