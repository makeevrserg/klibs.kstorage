package ru.astrainteractive.klibs.kstorage.suspend

import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class SampleDataStoreProviderTest {
    @Test
    fun GIVEN_data_stora_provder_WHEN_read_write_THEN_read_written(): Unit = runTest {
        val default = 10
        val storeProvider = SampleDataStoreProvider(
            key = intPreferencesKey("key"),
            factory = { default }
        )
        assertEquals(default, storeProvider.provide().first())
        15.let { nextValue ->
            storeProvider.save(nextValue)
            assertEquals(nextValue, storeProvider.provide().first())
        }
    }
}