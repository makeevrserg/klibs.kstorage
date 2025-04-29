package ru.astrainteractive.klibs.kstorage.suspend.impl

import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import ru.astrainteractive.klibs.kstorage.suspend.SuspendMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.value.SuspendValueLoader
import ru.astrainteractive.klibs.kstorage.suspend.value.SuspendValueSaver

class DefaultSuspendMutableKrate<T>(
    private val factory: ValueFactory<T>,
    private val loader: SuspendValueLoader<T>,
    private val saver: SuspendValueSaver<T> = SuspendValueSaver.Empty(),
) : SuspendMutableKrate<T> {
    override suspend fun getValue(): T {
        val value = loader.loadAndGet() ?: factory.create()
        return value
    }

    override suspend fun save(value: T) {
        saver.save(value)
    }

    override suspend fun reset() {
        val default = factory.create()
        save(default)
    }
}
