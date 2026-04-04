package ru.astrainteractive.klibs.kstorage.suspend

import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import com.russhwolf.settings.observable.makeObservable
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import ru.astrainteractive.klibs.kstorage.settings.MapSettings
import ru.astrainteractive.klibs.kstorage.suspend.krate.SettingsFlowMutableKrate
import kotlin.test.Test
import kotlin.test.assertEquals

internal class FlowMutableKrateTest {
    private fun TestScope.createSettings(): FlowSettings {
        return MapSettings()
            .makeObservable()
            .toFlowSettings(UnconfinedTestDispatcher(testScheduler))
    }

    @Test
    fun GIVEN_10_as_default_value_and_loader_null_WHEN_load_THEN_return_default() = runTest {
        val factoryValue = 10
        val krate = SettingsFlowMutableKrate(
            factory = { factoryValue },
            key = "KEY_1",
            settings = createSettings()
        )
        val stateFlow = krate.stateFlow(
            backgroundScope,
            coroutineDispatcher = UnconfinedTestDispatcher(testScheduler)
        )
        assertEquals(
            factoryValue,
            stateFlow.first(),
            "StateFlow should emit factory value when loader is null"
        )
        assertEquals(
            factoryValue,
            krate.getValue(),
            "getValue() should return factory value when loader is null"
        )
        assertEquals(
            factoryValue,
            stateFlow.first(),
            "StateFlow should still emit factory value on subsequent reads"
        )
    }

    @Test
    fun GIVEN_one_as_default_another_as_loader_WHEN_load_THEN_return_loader() = runTest {
        val factoryValue = 15
        val krate = SettingsFlowMutableKrate(
            factory = { factoryValue },
            key = "KEY_2",
            settings = createSettings()
        )
        val stateFlow = krate.stateFlow(backgroundScope, coroutineDispatcher = UnconfinedTestDispatcher(testScheduler))
        assertEquals(
            factoryValue,
            stateFlow.first(),
            "StateFlow should emit factory value initially"
        )
        assertEquals(
            factoryValue,
            krate.getValue(),
            "getValue() should return factory value initially"
        )
        assertEquals(
            factoryValue,
            stateFlow.first(),
            "StateFlow should still emit factory value on subsequent reads"
        )
    }

    @Test
    fun GIVEN_empty_store_WHEN_save_and_reset_THEN_saved_and_reset() = runTest {
        val factoryValue = 10
        val krate = SettingsFlowMutableKrate(
            factory = { factoryValue },
            key = "KEY_3",
            settings = createSettings()
        )
        val stateFlow = krate.stateFlow(
            coroutineScope = backgroundScope,
            coroutineDispatcher = UnconfinedTestDispatcher(testScheduler)
        )
        assertEquals(
            factoryValue,
            stateFlow.first(),
            "StateFlow should emit factory value for empty store"
        )
        assertEquals(
            factoryValue,
            krate.getValue(),
            "getValue() should return factory value for empty store"
        )
        11.let { newValue ->
            krate.save(newValue)
            advanceUntilIdle()
            assertEquals(
                newValue,
                krate.getValue(),
                "getValue() should return new value after save"
            )
            assertEquals(
                newValue,
                stateFlow.first(),
                "StateFlow should emit new value after save"
            )
        }
        krate.reset()
        advanceUntilIdle()

        assertEquals(
            factoryValue,
            stateFlow.first(),
            "After reset StateFlow should be back to factory value"
        )
        assertEquals(
            factoryValue,
            krate.getValue(),
            "After reset getValue() should return factory value"
        )
    }

    @Test
    fun GIVEN_prefilled_store_WHEN_save_and_reset_THEN_saved_and_reset() = runTest {
        val factoryValue = 10
        val krate = SettingsFlowMutableKrate(
            factory = { factoryValue },
            key = "KEY_4",
            settings = createSettings()
        )
        val stateFlow = krate.stateFlow(
            coroutineScope = backgroundScope,
            coroutineDispatcher = UnconfinedTestDispatcher(testScheduler)
        )
        assertEquals(
            factoryValue,
            stateFlow.first(),
            "StateFlow should emit factory value initially"
        )
        assertEquals(
            factoryValue,
            krate.getValue(),
            "getValue() should return factory value initially"
        )
        assertEquals(
            factoryValue,
            stateFlow.first(),
            "StateFlow should still emit factory value on subsequent reads"
        )
        11.let { newValue ->
            krate.save(newValue)
            advanceUntilIdle()
            assertEquals(
                newValue,
                stateFlow.first(),
                "StateFlow should emit new value after save"
            )
            assertEquals(
                newValue,
                krate.getValue(),
                "getValue() should return new value after save"
            )
        }
        krate.reset()
        advanceUntilIdle()
        assertEquals(
            factoryValue,
            stateFlow.first(),
            "After reset krate should be back to factory value"
        )
        assertEquals(
            factoryValue,
            krate.getValue(),
            "After reset StateFlow should be back to factory value"
        )
    }
}
