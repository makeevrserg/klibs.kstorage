package ru.astrainteractive.klibs.kstorage.suspend

import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.test.runTest
import ru.astrainteractive.klibs.kstorage.suspend.test.DataStoreFlowMutableKrate
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test cases:
 * 1. Factory value not null, loader is null.
 * 2. Factory value is null, loader not null
 * 3. Factory value is one, the loader is another
 * 4. Factory value is null,m the loader is null
 */
internal class FlowSuspendFlowStateFlowSuspendMutableKrateKrateKrateKrateTest {
    @Test
    fun GIVEN_10_as_default_value_and_loader_null_WHEN_load_THEN_return_default() = runTest {
        val factoryValue = 10
        val krate = DataStoreFlowMutableKrate(
            factory = { factoryValue },
            key = intPreferencesKey("KEY_1")
        )
        assertEquals(factoryValue, krate.cachedValue)
        assertEquals(factoryValue, krate.loadAndGet())
        assertEquals(factoryValue, krate.cachedValue)
    }

    @Test
    fun GIVEN_one_as_default_another_as_loader_WHEN_load_THEN_return_loader() = runTest {
        val factoryValue = 15
        val krate = DataStoreFlowMutableKrate(
            factory = { factoryValue },
            key = intPreferencesKey("KEY_2")
        )
        assertEquals(factoryValue, krate.cachedValue)
        assertEquals(factoryValue, krate.loadAndGet())
        assertEquals(factoryValue, krate.cachedValue)
    }

    @Test
    fun GIVEN_empty_store_WHEN_save_and_reset_THEN_saved_and_reset() = runTest {
        val factoryValue = 10
        val krate = DataStoreFlowMutableKrate(
            factory = { factoryValue },
            key = intPreferencesKey("KEY_3")
        )
        assertEquals(factoryValue, krate.cachedValue)
        assertEquals(factoryValue, krate.loadAndGet())
        11.let { newValue ->
            krate.save(newValue)
            assertEquals(newValue, krate.cachedValue)
            assertEquals(newValue, krate.loadAndGet())
        }
        krate.reset()
        assertEquals(factoryValue, krate.cachedValue)
        assertEquals(factoryValue, krate.loadAndGet())
    }

    @Test
    fun GIVEN_prefilled_store_WHEN_save_and_reset_THEN_saved_and_reset() = runTest {
        val factoryValue = 10
        val krate = DataStoreFlowMutableKrate(
            factory = { factoryValue },
            key = intPreferencesKey("KEY_4")
        )
        assertEquals(factoryValue, krate.cachedValue)
        assertEquals(factoryValue, krate.loadAndGet())
        assertEquals(factoryValue, krate.cachedValue)
        11.let { newValue ->
            krate.save(newValue)
            assertEquals(newValue, krate.cachedValue)
            assertEquals(newValue, krate.loadAndGet())
        }
        krate.reset()
        assertEquals(factoryValue, krate.cachedValue)
        assertEquals(factoryValue, krate.loadAndGet())
    }
}
