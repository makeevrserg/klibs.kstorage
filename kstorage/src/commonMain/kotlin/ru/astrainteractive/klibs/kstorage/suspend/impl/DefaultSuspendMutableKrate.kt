package ru.astrainteractive.klibs.kstorage.suspend.impl

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.astrainteractive.klibs.kstorage.api.provider.ValueFactory
import ru.astrainteractive.klibs.kstorage.suspend.StateFlowSuspendKrate
import ru.astrainteractive.klibs.kstorage.suspend.provider.SuspendValueLoader
import ru.astrainteractive.klibs.kstorage.suspend.provider.SuspendValueSaver

class DefaultSuspendMutableKrate<T>(
    private val factory: ValueFactory<T>,
    private val loader: SuspendValueLoader<T>,
    private val saver: SuspendValueSaver<T> = SuspendValueSaver.Empty()
) : StateFlowSuspendKrate.Mutable<T> {
    private val _cachedStateFlow = MutableStateFlow(factory.create())
    override val cachedStateFlow: StateFlow<T> = _cachedStateFlow.asStateFlow()

    override suspend fun loadAndGet(): T {
        val value = loader.loadAndGet() ?: factory.create()
        _cachedStateFlow.value = value
        return value
    }

    override suspend fun save(value: T) {
        if (saver is SuspendValueSaver.Empty) return
        saver.save(value)
        _cachedStateFlow.value = value
    }

    override suspend fun reset() {
        val default = factory.create()
        save(default)
    }
}
