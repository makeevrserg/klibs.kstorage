package ru.astrainteractive.klibs.kstorage.api.impl

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import ru.astrainteractive.klibs.kstorage.api.LockOwner
import ru.astrainteractive.klibs.kstorage.api.MutableKrate
import ru.astrainteractive.klibs.kstorage.api.StateFlowMutableKrate
import ru.astrainteractive.klibs.kstorage.api.reuseLock

class DefaultStateFlowMutableKrate<T>(
    private val instance: MutableKrate<T>,
) : StateFlowMutableKrate<T>, LockOwner {
    override val lock = instance.reuseLock()
    private var _cachedStateFlow = lock.withLock {
        MutableStateFlow(instance.getValue())
    }

    override val cachedStateFlow: StateFlow<T>
        get() = _cachedStateFlow

    override val cachedValue: T
        get() = cachedStateFlow.value

    override fun save(value: T) {
        lock.withLock {
            _cachedStateFlow.update { value }
            instance.save(value)
        }
    }

    override fun reset() {
        lock.withLock {
            _cachedStateFlow.update { instance.resetAndGet() }
        }
    }

    override fun resetAndGet(): T {
        return lock.withLock {
            val currentValue = instance.resetAndGet()
            _cachedStateFlow.update { currentValue }
            currentValue
        }
    }

    override fun save(block: (T) -> T) {
        return lock.withLock {
            val currentValue = instance.saveAndGet(block)
            _cachedStateFlow.update { currentValue }
        }
    }

    override fun saveAndGet(block: (T) -> T): T {
        return lock.withLock {
            val currentValue = instance.saveAndGet(block)
            _cachedStateFlow.update { currentValue }
            currentValue
        }
    }

    override fun getValue(): T {
        return lock.withLock {
            val value = instance.getValue()
            _cachedStateFlow.update { value }
            value
        }
    }
}
