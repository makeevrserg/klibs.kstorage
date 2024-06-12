package ru.astrainteractive.klibs.kstorage.suspend

import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import ru.astrainteractive.klibs.kstorage.suspend.test.DataStoreFlowMutableKrate
import kotlin.test.Test
import kotlin.test.assertEquals

internal class FlowMutableKrateTest {
    @Test
    fun test(): Unit = runTest {
        val initialValue = 10
        val krate = DataStoreFlowMutableKrate(
            key = intPreferencesKey("some_int_key"),
            factory = { initialValue }
        )
        assertEquals(initialValue, krate.flow.first())
        assertEquals(initialValue, krate.getValue())
        15.let { newValue ->
            krate.save(newValue)
            assertEquals(newValue, krate.flow.first())
        }
        krate.reset()
        assertEquals(initialValue, krate.flow.first())
    }
}
