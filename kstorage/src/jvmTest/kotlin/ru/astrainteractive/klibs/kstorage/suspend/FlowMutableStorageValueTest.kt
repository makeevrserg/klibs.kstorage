package ru.astrainteractive.klibs.kstorage.suspend

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okio.FileSystem
import ru.astrainteractive.klibs.kstorage.suspend.value.SuspendValueFactory
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

internal class FlowMutableStorageValueTest {
    private fun createFlowStorageValue(
        key: String,
        factory: SuspendValueFactory<Int>
    ): DataStoreStorageValue {
        val dataStore = PreferenceDataStoreFactory
            .createWithPath { FileSystem.SYSTEM_TEMPORARY_DIRECTORY / "$key.preferences_pb" }
        return DataStoreStorageValue(
            dataStore = dataStore,
            key = key,
            factory = factory
        )
    }

    @Test
    fun test() {
        runTest {
            val initialValue = 10
            val storageValue = createFlowStorageValue(
                key = "key_${Random.nextInt()}",
                factory = { initialValue }
            )
            // load initials
            assertEquals(initialValue, storageValue.flow.first())
            assertEquals(initialValue, storageValue.load())
            // save next
            15.let { newValue ->
                storageValue.save(newValue)
                assertEquals(newValue, storageValue.flow.first())
            }
            // reset
            storageValue.reset()
            assertEquals(initialValue, storageValue.flow.first())
        }
    }
}
