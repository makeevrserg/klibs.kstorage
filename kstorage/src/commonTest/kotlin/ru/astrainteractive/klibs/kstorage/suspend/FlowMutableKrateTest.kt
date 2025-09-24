package ru.astrainteractive.klibs.kstorage.suspend

import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import com.russhwolf.settings.observable.makeObservable
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import ru.astrainteractive.klibs.kstorage.suspend.util.DataStoreFlowMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.util.MapSettings
import kotlin.test.Test
import kotlin.test.assertEquals

internal class FlowMutableKrateTest {
    private fun createSettings(): FlowSettings {
        return MapSettings()
            .makeObservable()
            .toFlowSettings(UnconfinedTestDispatcher())
    }

    @Test
    fun GIVEN_10_as_default_value_and_loader_null_WHEN_load_THEN_return_default() = runTest {
        val factoryValue = 10
        val krate = DataStoreFlowMutableKrate(
            factory = { factoryValue },
            key = "KEY_1",
            settings = createSettings()
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
            key = "KEY_2",
            settings = createSettings()
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
            key = "KEY_3",
            settings = createSettings()
        )
        val stateFlow = krate.stateFlow(backgroundScope)
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
            key = "KEY_4",
            settings = createSettings()
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
