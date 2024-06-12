package ru.astrainteractive.klibs.kstorage.suspend.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.astrainteractive.klibs.kstorage.suspend.FlowMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.provider.FlowProvider
import ru.astrainteractive.klibs.kstorage.suspend.provider.SuspendValueFactory
import ru.astrainteractive.klibs.kstorage.suspend.provider.SuspendValueSaver

class DefaultFlowMutableKrate<T>(
    private val factory: SuspendValueFactory<T>,
    private val loader: FlowProvider<T>,
    private val saver: SuspendValueSaver<T> = SuspendValueSaver.Empty()
) : FlowMutableKrate<T> {

    override val flow: Flow<T> = loader.provide()
        .map { value -> value ?: factory.create() }

    override suspend fun loadAndGet(): T {
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
