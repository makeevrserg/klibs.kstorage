package ru.astrainteractive.klibs.kstorage.suspend.impl

import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import ru.astrainteractive.klibs.kstorage.internal.lock.Lock
import ru.astrainteractive.klibs.kstorage.suspend.SuspendMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.value.SuspendValueLoader
import ru.astrainteractive.klibs.kstorage.suspend.value.SuspendValueSaver

class DefaultSuspendMutableKrate<T>(
    private val factory: ValueFactory<T>,
    private val loader: SuspendValueLoader<T>,
    private val saver: SuspendValueSaver<T> = SuspendValueSaver.Empty(),
) : SuspendMutableKrate<T> {
    private val lock = Lock()

    override suspend fun getValue(): T {
        return lock.withSuspendLock { loader.loadAndGet() ?: factory.create() }
    }

    override suspend fun save(value: T) {
        lock.withSuspendLock { saver.save(value) }
    }

    override suspend fun save(block: suspend (T) -> T) {
        lock.withSuspendLock {
            val oldValue = getValue()
            val newValue = block.invoke(oldValue)
            save(newValue)
        }
    }

    override suspend fun reset() {
        lock.withSuspendLock {
            val default = factory.create()
            saver.save(default)
        }
    }

    override suspend fun resetAndGet(): T {
        return lock.withSuspendLock {
            val default = factory.create()
            saver.save(default)
            default
        }
    }

    override suspend fun saveAndGet(block: suspend (T) -> T): T {
        return lock.withSuspendLock {
            val oldValue = getValue()
            val newValue = block.invoke(oldValue)
            save(newValue)
            newValue
        }
    }
}
