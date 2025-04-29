package ru.astrainteractive.klibs.kstorage.api.impl

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import ru.astrainteractive.klibs.kstorage.api.Krate
import ru.astrainteractive.klibs.kstorage.api.StateFlowKrate

/**
 * This [DefaultStateFlowKrate] can be used with delegation
 *
 * If false will put [factory] value into [cachedValue]
 */
class DefaultStateFlowKrate<T>(
    private val instance: Krate<T>,
) : StateFlowKrate<T> {
    private var _cachedStateFlow = MutableStateFlow(instance.getValue())

    override val cachedStateFlow: StateFlow<T>
        get() = _cachedStateFlow

    override val cachedValue: T
        get() = cachedStateFlow.value

    override fun getValue(): T {
        val value = instance.getValue()
        _cachedStateFlow.update { value }
        return value
    }
}
