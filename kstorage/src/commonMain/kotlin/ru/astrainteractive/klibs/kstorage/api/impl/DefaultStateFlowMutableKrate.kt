package ru.astrainteractive.klibs.kstorage.api.impl

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.astrainteractive.klibs.kstorage.api.StateFlowMutableKrate
import ru.astrainteractive.klibs.kstorage.api.provider.DefaultValueFactory
import ru.astrainteractive.klibs.kstorage.api.provider.ValueLoader
import ru.astrainteractive.klibs.kstorage.api.provider.ValueSaver

/**
 * This DefaultFlowStorageValue<T> can be used with delegation
 */
class DefaultStateFlowMutableKrate<T>(
    private val factory: DefaultValueFactory<T>,
    private val saver: ValueSaver<T> = ValueSaver.Empty(),
    private val loader: ValueLoader<T>,
) : StateFlowMutableKrate<T> {
    private val _stateFlow = MutableStateFlow(loader.loadAndGet() ?: factory.create())

    override val cachedStateFlow: StateFlow<T> = _stateFlow.asStateFlow()

    override fun save(value: T) {
        if (saver is ValueSaver.Empty) return
        saver.save(value)
        _stateFlow.value = value
    }

    override fun loadAndGet(): T {
        _stateFlow.value = loader.loadAndGet() ?: factory.create()
        return cachedValue
    }

    override fun reset() {
        val defaultValue = factory.create()
        save(defaultValue)
    }
}
