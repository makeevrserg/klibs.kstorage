@file:Suppress("FunctionNaming")

package ru.astrainteractive.klibs.kstorage

import ru.astrainteractive.klibs.kstorage.api.MutableStorageValue
import ru.astrainteractive.klibs.kstorage.api.StateFlowMutableStorageValue
import ru.astrainteractive.klibs.kstorage.impl.MutableStorageValueImpl
import ru.astrainteractive.klibs.kstorage.impl.StateFlowMutableStorageValueImpl

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
    return StateFlowMutableStorageValueImpl(
        default = default,
        loadSettingsValue = loadSettingsValue,
        saveSettingsValue = saveSettingsValue
    )
}

fun <T> StateFlowMutableStorageValue(
    default: T
): StateFlowMutableStorageValue<T> = StateFlowMutableStorageValueImpl(
    default = default,
    loadSettingsValue = { default },
    saveSettingsValue = {}
)
