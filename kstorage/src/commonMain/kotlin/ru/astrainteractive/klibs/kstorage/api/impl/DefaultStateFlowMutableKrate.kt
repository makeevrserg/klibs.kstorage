package ru.astrainteractive.klibs.kstorage.api.impl

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.astrainteractive.klibs.kstorage.api.StateFlowKrate
import ru.astrainteractive.klibs.kstorage.api.provider.ValueFactory
import ru.astrainteractive.klibs.kstorage.api.provider.ValueLoader
import ru.astrainteractive.klibs.kstorage.api.provider.ValueSaver

/**
 * This [DefaultStateFlowMutableKrate] can be used with delegation
 *
 * If false will put [factory] value into [cachedValue]
 */
class DefaultStateFlowMutableKrate<T>(
    private val factory: ValueFactory<T>,
    private val saver: ValueSaver<T> = ValueSaver.Empty(),
    private val loader: ValueLoader<T>,
) : StateFlowKrate.Mutable<T> {

    private val _stateFlow = MutableStateFlow(loader.loadAndGet() ?: factory.create())

    override val cachedStateFlow: StateFlow<T> = _stateFlow.asStateFlow()

    override fun save(value: T) {
        saver.save(value)
        _stateFlow.update { value }
    }

    override fun loadAndGet(): T {
        _stateFlow.update { loader.loadAndGet() ?: factory.create() }
        return cachedValue
    }

    override fun reset() {
        val defaultValue = factory.create()
        save(defaultValue)
    }
}
