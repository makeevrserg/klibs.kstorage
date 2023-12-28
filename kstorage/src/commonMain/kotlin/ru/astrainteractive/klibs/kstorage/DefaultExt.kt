package ru.astrainteractive.klibs.kstorage

import ru.astrainteractive.klibs.kstorage.api.MutableStorageValue
import ru.astrainteractive.klibs.kstorage.api.StateFlowMutableStorageValue

/**
 * [setDefault] is used to convert nullable to non nullable
 */
fun <T : Any> MutableStorageValue<T?>.withDefault(default: T): MutableStorageValue<T> {
    return MutableStorageValue(
        default = default,
        saveSettingsValue = { save(it) },
        loadSettingsValue = { load() ?: default }
    )
}

/**
 * [setDefault] is used to convert nullable to non nullable
 */
fun <T : Any> StateFlowMutableStorageValue<T?>.withDefault(default: T): StateFlowMutableStorageValue<T> {
    return StateFlowMutableStorageValue(
        default = default,
        saveSettingsValue = { save(it) },
        loadSettingsValue = { load() ?: default }
    )
}
