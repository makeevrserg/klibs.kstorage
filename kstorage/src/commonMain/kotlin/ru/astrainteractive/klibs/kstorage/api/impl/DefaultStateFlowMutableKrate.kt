package ru.astrainteractive.klibs.kstorage.api.impl

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.astrainteractive.klibs.kstorage.api.StateFlowMutableKrate
import ru.astrainteractive.klibs.kstorage.api.provider.ValueFactory
import ru.astrainteractive.klibs.kstorage.api.provider.ValueLoader
import ru.astrainteractive.klibs.kstorage.api.provider.ValueSaver

/**
 * This [DefaultStateFlowMutableKrate] can be used with delegation
 *
 * @param requireInstantLoading - if true, will load value into [cachedValue] directly from [loader].
 * If false will put [factory] value into [cachedValue]
 */
class DefaultStateFlowMutableKrate<T>(
    private val factory: ValueFactory<T>,
    private val saver: ValueSaver<T> = ValueSaver.Empty(),
    private val loader: ValueLoader<T>,
    requireInstantLoading: Boolean = true
) : StateFlowMutableKrate<T> {

    private val _stateFlow = MutableStateFlow(
        value = when {
            requireInstantLoading -> loader.loadAndGet() ?: factory.create()
            else -> factory.create()
        }
    )

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
