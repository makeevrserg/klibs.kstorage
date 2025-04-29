package ru.astrainteractive.klibs.kstorage.suspend.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import ru.astrainteractive.klibs.kstorage.suspend.FlowMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.value.FlowProvider
import ru.astrainteractive.klibs.kstorage.suspend.value.SuspendValueSaver

class DefaultFlowMutableKrate<T>(
    private val factory: ValueFactory<T>,
    private val loader: FlowProvider<T>,
    private val saver: SuspendValueSaver<T> = SuspendValueSaver.Empty(),
) : FlowMutableKrate<T> {
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
        val value = loader.provide().first() ?: factory.create()
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
