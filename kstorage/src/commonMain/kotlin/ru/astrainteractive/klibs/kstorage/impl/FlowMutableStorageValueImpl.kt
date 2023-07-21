package ru.astrainteractive.klibs.kstorage.impl

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.astrainteractive.klibs.kstorage.api.StateFlowMutableStorageValue

/**
 * This DefaultFlowStorageValue<T> can be used with delegation
 */
internal class FlowMutableStorageValueImpl<T>(
    private val default: T,
    private val loadSettingsValue: () -> T,
    private val saveSettingsValue: (T) -> Unit
) : StateFlowMutableStorageValue<T> {

    private val mutableStateFlow by lazy {
        MutableStateFlow(loadSettingsValue.invoke())
    }
    override val stateFlow: StateFlow<T>
        get() = mutableStateFlow

    override fun load(): T {
        val newValue = loadSettingsValue.invoke()
        mutableStateFlow.value = newValue
        return newValue
    }

    override fun save(value: T) {
        saveSettingsValue.invoke(value)
        mutableStateFlow.value = value
    }

    override fun reset() {
        save(default)
    }

    override fun update(block: (T) -> T) {
        save(block(value))
    }
}
