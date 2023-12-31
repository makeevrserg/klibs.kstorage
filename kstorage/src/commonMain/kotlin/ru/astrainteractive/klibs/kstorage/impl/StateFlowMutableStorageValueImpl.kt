package ru.astrainteractive.klibs.kstorage.impl

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.astrainteractive.klibs.kstorage.api.MutableStorageValue
import ru.astrainteractive.klibs.kstorage.api.StateFlowMutableStorageValue

/**
 * This DefaultFlowStorageValue<T> can be used with delegation
 */
internal class StateFlowMutableStorageValueImpl<T>(
    private val default: T,
    private val loadSettingsValue: () -> T,
    private val saveSettingsValue: (T) -> Unit,
    private val mutableStateFlow: MutableStateFlow<T> = MutableStateFlow(loadSettingsValue.invoke())
) : StateFlowMutableStorageValue<T>,
    MutableStorageValue<T> by MutableStorageValueImpl(
        default = default,
        loadSettingsValue = loadSettingsValue,
        saveSettingsValue = saveSettingsValue,
        onChanged = { mutableStateFlow.value = it }
    ) {
    override val stateFlow: StateFlow<T>
        get() = mutableStateFlow
    override val value: T
        get() = stateFlow.value
}
