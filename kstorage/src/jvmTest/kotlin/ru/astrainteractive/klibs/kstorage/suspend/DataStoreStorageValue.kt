package ru.astrainteractive.klibs.kstorage.suspend

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.map
import ru.astrainteractive.klibs.kstorage.suspend.impl.FlowMutableStorageValueImpl
import ru.astrainteractive.klibs.kstorage.suspend.value.SuspendValueFactory

internal class DataStoreStorageValue(
    private val dataStore: DataStore<Preferences>,
    key: String,
    factory: SuspendValueFactory<Int>,
) : FlowMutableStorageValue<Int> by FlowMutableStorageValueImpl(
    factory = factory,
    loader = {
        dataStore.data.map {
            it[intPreferencesKey(key)] ?: factory.create()
        }
    },
    saver = { value ->
        dataStore.edit {
            it[intPreferencesKey(key)] = value
        }
    }
)
