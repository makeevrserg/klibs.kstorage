package ru.astrainteractive.klibs.kstorage.suspend

import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import ru.astrainteractive.klibs.kstorage.suspend.test.DataStoreFlowMutableKrate
import kotlin.test.Test
import kotlin.test.assertEquals

internal class FlowSuspendFlowStateFlowSuspendMutableKrateKrateKrateKrateTest {
    @Test
    fun GIVEN_10_as_default_value_and_loader_null_WHEN_load_THEN_return_default() = runTest {
        val factoryValue = 10
        val krate = DataStoreFlowMutableKrate(
            factory = { factoryValue },
            key = intPreferencesKey("KEY_1")
        )
        val stateFlow = krate.stateFlow(TestScope())
        assertEquals(factoryValue, stateFlow.value)
        assertEquals(factoryValue, krate.getValue())
        assertEquals(factoryValue, stateFlow.value)
    }

    @Test
    fun GIVEN_one_as_default_another_as_loader_WHEN_load_THEN_return_loader() = runTest {
        val factoryValue = 15
        val krate = DataStoreFlowMutableKrate(
            factory = { factoryValue },
            key = intPreferencesKey("KEY_2")
        )
        val stateFlow = krate.stateFlow(TestScope())
        assertEquals(factoryValue, stateFlow.value)
        assertEquals(factoryValue, krate.getValue())
        assertEquals(factoryValue, stateFlow.value)
    }

    @Test
    fun GIVEN_empty_store_WHEN_save_and_reset_THEN_saved_and_reset() = runTest {
        val factoryValue = 10
        val krate = DataStoreFlowMutableKrate(
            factory = { factoryValue },
            key = intPreferencesKey("KEY_3")
        )
        val stateFlow = krate.stateFlow(TestScope())
        assertEquals(factoryValue, stateFlow.value)
        assertEquals(factoryValue, krate.getValue())
        11.let { newValue ->
            krate.save(newValue)
            assertEquals(newValue, stateFlow.value)
            assertEquals(newValue, krate.getValue())
        }
        krate.reset()
        assertEquals(factoryValue, stateFlow.value)
        assertEquals(factoryValue, krate.getValue())
    }

    @Test
    fun GIVEN_prefilled_store_WHEN_save_and_reset_THEN_saved_and_reset() = runTest {
        val factoryValue = 10
        val krate = DataStoreFlowMutableKrate(
            factory = { factoryValue },
            key = intPreferencesKey("KEY_4")
        )
        val stateFlow = krate.stateFlow(TestScope())
        assertEquals(factoryValue, stateFlow.value)
        assertEquals(factoryValue, krate.getValue())
        assertEquals(factoryValue, stateFlow.value)
        11.let { newValue ->
            krate.save(newValue)
            assertEquals(newValue, stateFlow.value)
            assertEquals(newValue, krate.getValue())
        }
        krate.reset()
        assertEquals(factoryValue, stateFlow.value)
        assertEquals(factoryValue, krate.getValue())
    }
}
