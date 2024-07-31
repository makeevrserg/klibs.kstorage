package ru.astrainteractive.klibs.kstorage.api.impl

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.astrainteractive.klibs.kstorage.api.cache.LoadingStarted
import ru.astrainteractive.klibs.kstorage.api.coroutine.StateFlowMutableKrate
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import ru.astrainteractive.klibs.kstorage.api.value.ValueLoader
import ru.astrainteractive.klibs.kstorage.api.value.ValueSaver

/**
 * This [DefaultStateFlowMutableKrate] can be used with delegation
 *
 * If false will put [factory] value into [cachedValue]
 */
class DefaultStateFlowMutableKrate<T>(
    private val factory: ValueFactory<T>,
    private val saver: ValueSaver<T> = ValueSaver.Empty(),
    private val loader: ValueLoader<T>,
    loadingStarted: LoadingStarted = LoadingStarted.Instantly
) : StateFlowMutableKrate<T> {

    private val _stateFlow = when (loadingStarted) {
        LoadingStarted.Instantly -> loader.loadAndGet() ?: factory.create()
        LoadingStarted.Manually -> factory.create()
    }.let(::MutableStateFlow)

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
