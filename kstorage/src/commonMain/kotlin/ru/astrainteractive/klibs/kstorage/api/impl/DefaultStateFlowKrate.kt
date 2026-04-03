package ru.astrainteractive.klibs.kstorage.api.impl

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import ru.astrainteractive.klibs.kstorage.api.Krate
import ru.astrainteractive.klibs.kstorage.api.LockOwner
import ru.astrainteractive.klibs.kstorage.api.StateFlowKrate
import ru.astrainteractive.klibs.kstorage.api.reuseLock

class DefaultStateFlowKrate<T>(
    private val instance: Krate<T>,
) : StateFlowKrate<T>, LockOwner {
    override val lock = instance.reuseLock()
    private var _cachedStateFlow = lock.withLock {
        MutableStateFlow(instance.getValue())
    }

    override val cachedStateFlow: StateFlow<T>
        get() = _cachedStateFlow

    override val cachedValue: T
        get() = cachedStateFlow.value

    override fun getValue(): T {
        return lock.withLock {
            val value = instance.getValue()
            _cachedStateFlow.update { value }
            value
        }
    }
}
