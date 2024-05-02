package ru.astrainteractive.klibs.kstorage.impl

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.astrainteractive.klibs.kstorage.api.StateFlowMutableStorageValue
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import ru.astrainteractive.klibs.kstorage.api.value.ValueLoader
import ru.astrainteractive.klibs.kstorage.api.value.ValueSaver

/**
 * This DefaultFlowStorageValue<T> can be used with delegation
 */
internal class StateFlowMutableStorageValueImpl<T>(
    private val factory: ValueFactory<T>,
    private val saver: ValueSaver<T>,
    private val loader: ValueLoader<T>,
) : StateFlowMutableStorageValue<T> {
    private val _stateFlow = MutableStateFlow(loader.load())

    override fun save(value: T) {
        saver.save(value)
        _stateFlow.value = value
    }

    override val stateFlow: StateFlow<T> = _stateFlow.asStateFlow()

    override fun reset() {
        save(factory.create())
    }

    override fun load(): T {
        _stateFlow.value = loader.load()
        return value
    }
}
