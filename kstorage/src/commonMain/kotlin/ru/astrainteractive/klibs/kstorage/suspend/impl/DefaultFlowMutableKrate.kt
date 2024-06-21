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
import ru.astrainteractive.klibs.kstorage.api.provider.ValueFactory
import ru.astrainteractive.klibs.kstorage.suspend.FlowMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.provider.FlowProvider
import ru.astrainteractive.klibs.kstorage.suspend.provider.SuspendValueSaver

class DefaultFlowMutableKrate<T>(
    private val factory: ValueFactory<T>,
    private val loader: FlowProvider<T>,
    private val saver: SuspendValueSaver<T> = SuspendValueSaver.Empty()
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
        return loader.provide().first() ?: factory.create()
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
