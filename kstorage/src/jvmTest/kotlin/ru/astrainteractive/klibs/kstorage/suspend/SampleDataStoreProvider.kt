package ru.astrainteractive.klibs.kstorage.suspend

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.FileSystem
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultFlowMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.provider.FlowProvider
import ru.astrainteractive.klibs.kstorage.suspend.provider.SuspendValueFactory
import ru.astrainteractive.klibs.kstorage.suspend.provider.SuspendValueSaver

internal class SampleDataStoreProvider<T>(
    private val key: Preferences.Key<T>,
    factory: SuspendValueFactory<T>
) : FlowProvider<T>,
    SuspendValueFactory<T> by factory,
    SuspendValueSaver<T> {
    private val path = FileSystem.SYSTEM_TEMPORARY_DIRECTORY / "$key.preferences_pb"
    private val dataStore = PreferenceDataStoreFactory
        .createWithPath {
            val file = path.toFile()
            if (file.exists()) file.delete()
            path
        }

    override fun provide(): Flow<T> = dataStore.data
        .map { preferences -> preferences[key] ?: create() }

    override suspend fun save(value: T) {
        dataStore.edit { preferences ->
            if (value == null) preferences.remove(key)
            else preferences[key] = value
        }
    }

    fun toKrate(): FlowMutableKrate<T> = DefaultFlowMutableKrate(
        factory = this,
        loader = this,
        saver = this
    )
}