package ru.astrainteractive.klibs.kstorage.api.impl

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import ru.astrainteractive.klibs.kstorage.api.MutableKrate
import ru.astrainteractive.klibs.kstorage.api.StateFlowMutableKrate

/**
 * This [DefaultStateFlowMutableKrate] can be used with delegation
 *
 * If false will put [factory] value into [cachedValue]
 */
class DefaultStateFlowMutableKrate<T>(
    private val instance: MutableKrate<T>,
) : StateFlowMutableKrate<T> {
    private var _cachedStateFlow = MutableStateFlow(instance.getValue())

    override val cachedStateFlow: StateFlow<T>
        get() = _cachedStateFlow

    override val cachedValue: T
        get() = cachedStateFlow.value

    override fun save(value: T) {
        _cachedStateFlow.update { value }
        instance.save(value)
    }

    override fun reset() {
        instance.reset()
        _cachedStateFlow.update { instance.getValue() }
    }

    override fun getValue(): T {
        val value = instance.getValue()
        _cachedStateFlow.update { value }
        return value
    }
}
