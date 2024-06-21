package ru.astrainteractive.klibs.kstorage.suspend.test

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map
import okio.FileSystem
import ru.astrainteractive.klibs.kstorage.api.provider.ValueFactory
import ru.astrainteractive.klibs.kstorage.suspend.FlowMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultFlowMutableKrate

internal class DataStoreFlowMutableKrate<T>(
    key: Preferences.Key<T>,
    dataStore: DataStore<Preferences> = PreferenceDataStoreFactory
        .createWithPath {
            val path = FileSystem.SYSTEM_TEMPORARY_DIRECTORY / "$key.preferences_pb"
            val file = path.toFile()
            if (file.exists()) file.delete()
            path
        },
    factory: ValueFactory<T>,
) : FlowMutableKrate<T> by DefaultFlowMutableKrate(
    factory = factory,
    loader = { dataStore.data.map { it[key] } },
    saver = { value ->
        dataStore.edit { preferences ->
            if (value == null) {
                preferences.remove(key)
            } else {
                preferences[key] = value
            }
        }
    }
)
