package ru.astrainteractive.klibs.kstorage.suspend.impl

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import ru.astrainteractive.klibs.kstorage.internal.lock.LockOwner
import ru.astrainteractive.klibs.kstorage.suspend.StateFlowSuspendMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.value.SuspendValueLoader
import ru.astrainteractive.klibs.kstorage.suspend.value.SuspendValueSaver
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class DefaultStateFlowSuspendMutableKrate<T>(
    private val factory: ValueFactory<T>,
    private val loader: SuspendValueLoader<T>,
    private val saver: SuspendValueSaver<T> = SuspendValueSaver.Empty(),
    coroutineContext: CoroutineContext = EmptyCoroutineContext
) : StateFlowSuspendMutableKrate<T>, LockOwner by LockOwner.Default() {
    private val _cachedStateFlow = lock.withLock { MutableStateFlow(factory.create()) }
    override val cachedStateFlow: StateFlow<T> = _cachedStateFlow.asStateFlow()

    override suspend fun getValue(): T {
        return lock.withSuspendLock {
            val value = loader.loadAndGet() ?: factory.create()
            _cachedStateFlow.update { value }
            value
        }
    }

    override suspend fun save(value: T) {
        lock.withSuspendLock {
            _cachedStateFlow.update { value }
            saver.save(value)
        }
    }

    override suspend fun save(block: suspend (T) -> T) {
        lock.withSuspendLock {
            val oldValue = getValue()
            val newValue = block.invoke(oldValue)
            _cachedStateFlow.update { newValue }
            saver.save(newValue)
        }
    }

    override suspend fun saveAndGet(block: suspend (T) -> T): T {
        return lock.withSuspendLock {
            val oldValue = loader.loadAndGet() ?: factory.create()
            val newValue = block.invoke(oldValue)
            _cachedStateFlow.update { newValue }
            saver.save(newValue)
            newValue
        }
    }

    override suspend fun reset() {
        lock.withSuspendLock {
            val default = factory.create()
            _cachedStateFlow.update { default }
            saver.save(default)
        }
    }

    override suspend fun resetAndGet(): T {
        return lock.withSuspendLock {
            val default = factory.create()
            _cachedStateFlow.update { default }
            saver.save(default)
            default
        }
    }

    init {
        val scope = CoroutineScope(SupervisorJob() + EmptyCoroutineContext)
        scope
            .launch(coroutineContext) { getValue() }
            .invokeOnCompletion { scope.cancel() }
            .dispose()
    }
}
