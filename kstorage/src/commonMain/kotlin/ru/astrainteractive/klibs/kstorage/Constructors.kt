@file:Suppress("FunctionNaming")

package ru.astrainteractive.klibs.kstorage

import ru.astrainteractive.klibs.kstorage.api.MutableStorageValue
import ru.astrainteractive.klibs.kstorage.api.StateFlowMutableStorageValue
import ru.astrainteractive.klibs.kstorage.impl.FlowMutableStorageValueImpl
import ru.astrainteractive.klibs.kstorage.impl.MutableStorageValueImpl

fun <T> InMemoryStateFlowMutableStorageValue(
    default: T
): StateFlowMutableStorageValue<T> = FlowMutableStorageValueImpl(
    default = default,
    loadSettingsValue = { default },
    saveSettingsValue = {}
)

fun <T> MutableStorageValue(
    default: T,
    loadSettingsValue: () -> T,
    saveSettingsValue: (T) -> Unit
): MutableStorageValue<T> {
    return MutableStorageValueImpl(
        default = default,
        loadSettingsValue = loadSettingsValue,
        saveSettingsValue = saveSettingsValue
    )
}

fun <T> StateFlowMutableStorageValue(
    default: T,
    loadSettingsValue: () -> T,
    saveSettingsValue: (T) -> Unit
): StateFlowMutableStorageValue<T> {
    return FlowMutableStorageValueImpl(
        default = default,
        loadSettingsValue = loadSettingsValue,
        saveSettingsValue = saveSettingsValue
    )
}
