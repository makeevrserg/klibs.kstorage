package ru.astrainteractive.klibs.kstorage.suspend

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import ru.astrainteractive.klibs.kstorage.settings.MapSettings
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultStateFlowSuspendMutableKrate
import kotlin.test.Test
import kotlin.test.assertEquals

internal class SuspendMutableKrateTest {
    @Test
    fun GIVEN_10_as_default_value_and_loader_null_WHEN_load_THEN_return_default() = runTest {
        val factoryValue = 10
        val store = MapSettings()
        val krate = DefaultStateFlowSuspendMutableKrate(
            factory = { factoryValue },
            saver = { store.putInt("KEY", it) },
            loader = { null },
            coroutineContext = StandardTestDispatcher(testScheduler)
        )
        assertEquals(
            expected = factoryValue,
            actual = krate.cachedStateFlow.first(),
            message = "StateFlow should emit factory value when loader returns null"
        )
        assertEquals(
            expected = factoryValue,
            actual = krate.getValue(),
            message = "getValue() should return factory value when loader returns null"
        )
        assertEquals(
            expected = factoryValue,
            actual = krate.cachedStateFlow.first(),
            message = "StateFlow should still emit factory value after getValue()"
        )
    }

    @Test
    fun GIVEN_null_as_default_10_as_loader_WHEN_load_THEN_return_loader() = runTest {
        val loaderValue = 10
        val store = MapSettings()
        val krate = DefaultStateFlowSuspendMutableKrate(
            factory = { null },
            saver = {
                if (it == null) {
                    store.remove("KEY")
                } else {
                    store.putInt("KEY", it)
                }
            },
            loader = { loaderValue },
            coroutineContext = StandardTestDispatcher(testScheduler)
        )
        assertEquals(
            expected = null,
            actual = krate.cachedStateFlow.first(),
            message = "StateFlow should initially emit null before getValue() is called"
        )
        assertEquals(
            expected = loaderValue,
            actual = krate.getValue(),
            message = "getValue() should return loader value when factory is null"
        )
        assertEquals(
            expected = loaderValue,
            actual = krate.cachedStateFlow.first(),
            message = "StateFlow should emit loader value after getValue() is called"
        )
    }

    @Test
    fun GIVEN_one_as_default_another_as_loader_WHEN_load_THEN_return_loader() = runTest {
        val loaderValue = 10
        val factoryValue = 15
        val store = MapSettings()
        val krate = DefaultStateFlowSuspendMutableKrate(
            factory = { factoryValue },
            saver = { store.putInt("KEY", it) },
            loader = { loaderValue },
            coroutineContext = StandardTestDispatcher(testScheduler)
        )
        assertEquals(
            expected = factoryValue,
            actual = krate.cachedStateFlow.first(),
            message = "StateFlow should initially emit factory value before load"
        )
        assertEquals(
            expected = loaderValue,
            actual = krate.getValue(),
            message = "getValue() should return loader value over factory value"
        )
        assertEquals(
            expected = loaderValue,
            actual = krate.cachedStateFlow.first(),
            message = "StateFlow should emit loader value after getValue()"
        )
    }

    @Test
    fun GIVEN_empty_store_WHEN_save_and_reset_THEN_saved_and_reset() = runTest {
        val factoryValue = 10
        val store = MapSettings()
        val krate = DefaultStateFlowSuspendMutableKrate(
            factory = { factoryValue },
            saver = { store.putInt("KEY", it) },
            loader = { store.getIntOrNull("KEY") },
            coroutineContext = StandardTestDispatcher(testScheduler)
        )
        assertEquals(
            expected = factoryValue,
            actual = krate.cachedStateFlow.first(),
            message = "StateFlow should emit factory value for empty store"
        )
        assertEquals(
            expected = factoryValue,
            actual = krate.getValue(),
            message = "getValue() should return factory value for empty store"
        )
        11.let { newValue ->
            krate.save(newValue)
            assertEquals(
                expected = newValue,
                actual = krate.cachedStateFlow.first(),
                message = "StateFlow should emit new value after save"
            )
            assertEquals(
                expected = newValue,
                actual = krate.getValue(),
                message = "getValue() should return new value after save"
            )
        }
        krate.reset()
        assertEquals(
            expected = factoryValue,
            actual = krate.cachedStateFlow.first(),
            message = "StateFlow should return to factory value after reset"
        )
        assertEquals(
            expected = factoryValue,
            actual = krate.getValue(),
            message = "getValue() should return factory value after reset"
        )
    }

    @Test
    fun GIVEN_prefilled_store_WHEN_save_and_reset_THEN_saved_and_reset() = runTest {
        val factoryValue = 10
        val defaultStoreValue = 15
        val store = MapSettings(mapOf("KEY" to defaultStoreValue))
        val krate = DefaultStateFlowSuspendMutableKrate(
            factory = { factoryValue },
            saver = { store.putInt("KEY", it) },
            loader = { store.getIntOrNull("KEY") },
            coroutineContext = StandardTestDispatcher(testScheduler)
        )
        assertEquals(
            expected = factoryValue,
            actual = krate.cachedStateFlow.first(),
            message = "StateFlow should initially emit factory value before load"
        )
        assertEquals(
            expected = defaultStoreValue,
            actual = krate.getValue(),
            message = "getValue() should return pre-filled store value"
        )
        assertEquals(
            expected = defaultStoreValue,
            actual = krate.cachedStateFlow.first(),
            message = "StateFlow should emit store value after getValue()"
        )
        11.let { newValue ->
            krate.save(newValue)
            assertEquals(
                expected = newValue,
                actual = krate.cachedStateFlow.first(),
                message = "StateFlow should emit new value after save"
            )
            assertEquals(
                expected = newValue,
                actual = krate.getValue(),
                message = "getValue() should return new value after save"
            )
        }
        store.remove("KEY")
        krate.reset()
        assertEquals(
            expected = factoryValue,
            actual = krate.cachedStateFlow.first(),
            message = "StateFlow should return to factory value after reset"
        )
        assertEquals(
            expected = factoryValue,
            actual = krate.getValue(),
            message = "getValue() should return factory value after reset"
        )
    }

    @Test
    fun GIVEN_manually_not_loaded_WHEN_getting_cached_THEN_loaded_not_factory() = runTest {
        val factoryValue = 10
        val loadedValue = 30
        val krate = DefaultStateFlowSuspendMutableKrate(
            factory = { factoryValue },
            saver = { },
            loader = { loadedValue },
            coroutineContext = StandardTestDispatcher(testScheduler)
        )
        advanceUntilIdle()
        assertEquals(
            expected = loadedValue,
            actual = krate.cachedStateFlow.first(),
            message = "StateFlow should eventually emit loaded value instead of factory value"
        )
    }
}
