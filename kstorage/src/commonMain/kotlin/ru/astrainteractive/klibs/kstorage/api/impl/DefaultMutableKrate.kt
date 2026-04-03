package ru.astrainteractive.klibs.kstorage.api.impl

import ru.astrainteractive.klibs.kstorage.api.MutableKrate
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import ru.astrainteractive.klibs.kstorage.api.value.ValueLoader
import ru.astrainteractive.klibs.kstorage.api.value.ValueSaver
import ru.astrainteractive.klibs.kstorage.internal.lock.LockOwner

class DefaultMutableKrate<T>(
    private val factory: ValueFactory<T>,
    private val saver: ValueSaver<T> = ValueSaver.Empty(),
    private val loader: ValueLoader<T>,
) : MutableKrate<T>, LockOwner by LockOwner.Default() {

    override fun getValue(): T {
        return lock.withLock {
            val newValue = loader.loadAndGet() ?: factory.create()
            newValue
        }
    }

    override fun save(value: T) {
        lock.withLock { saver.save(value) }
    }

    override fun save(block: (T) -> T) {
        lock.withLock {
            val currentValue = loader.loadAndGet() ?: factory.create()
            val newValue = block.invoke(currentValue)
            saver.save(newValue)
        }
    }

    override fun saveAndGet(block: (T) -> T): T {
        return lock.withLock {
            val currentValue = loader.loadAndGet() ?: factory.create()
            val newValue = block.invoke(currentValue)
            saver.save(newValue)
            newValue
        }
    }

    override fun reset() {
        lock.withLock {
            val defaultValue = factory.create()
            saver.save(defaultValue)
        }
    }

    override fun resetAndGet(): T {
        return lock.withLock {
            val defaultValue = factory.create()
            saver.save(defaultValue)
            defaultValue
        }
    }
}
