package ru.astrainteractive.klibs.kstorage.suspend.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import ru.astrainteractive.klibs.kstorage.internal.lock.LockOwner
import ru.astrainteractive.klibs.kstorage.suspend.FlowMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.value.FlowProvider
import ru.astrainteractive.klibs.kstorage.suspend.value.SuspendValueSaver

class DefaultFlowMutableKrate<T>(
    private val factory: ValueFactory<T>,
    private val loader: FlowProvider<T>,
    private val saver: SuspendValueSaver<T> = SuspendValueSaver.Empty(),
) : FlowMutableKrate<T>, LockOwner by LockOwner.Default() {

    override val flow: Flow<T> = loader.provide()
        .map { value -> value ?: factory.create() }

    override fun stateFlow(
        coroutineScope: CoroutineScope,
        sharingStarted: SharingStarted,
        dispatcher: CoroutineDispatcher
    ): StateFlow<T> = flow
        .flowOn(dispatcher)
        .stateIn(coroutineScope, sharingStarted, factory.create())

    override suspend fun getValue(): T {
        return lock.withSuspendLock {
            val value = flow
                .firstOrNull()
                ?: factory.create()
            value
        }
    }

    override suspend fun save(value: T) {
        lock.withSuspendLock {
            saver.save(value)
        }
    }

    override suspend fun save(block: suspend (T) -> T) {
        lock.withSuspendLock {
            val oldValue = flow
                .firstOrNull()
                ?: factory.create()
            val newValue = block.invoke(oldValue)
            saver.save(newValue)
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
            val oldValue = flow
                .firstOrNull()
                ?: factory.create()
            val newValue = block.invoke(oldValue)
            saver.save(newValue)
            newValue
        }
    }
}
