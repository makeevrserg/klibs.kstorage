package ru.astrainteractive.klibs.kstorage.suspend

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.timeout
import kotlinx.coroutines.test.runTest
import ru.astrainteractive.klibs.kstorage.settings.MapSettings
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultStateFlowSuspendMutableKrate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

internal class SuspendMutableKrateTest {
    @Test
    fun GIVEN_10_as_default_value_and_loader_null_WHEN_load_THEN_return_default() = runTest {
        val factoryValue = 10
        val store = MapSettings()
        val krate = DefaultStateFlowSuspendMutableKrate(
            factory = { factoryValue },
            saver = { store.putInt("KEY", it) },
            loader = { null },
            coroutineContext = coroutineContext
        )
        assertEquals(factoryValue, krate.cachedStateFlow.value)
        assertEquals(factoryValue, krate.getValue())
        assertEquals(factoryValue, krate.cachedStateFlow.value)
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
            coroutineContext = coroutineContext
        )
        assertEquals(null, krate.cachedStateFlow.value)
        assertEquals(loaderValue, krate.getValue())
        assertEquals(loaderValue, krate.cachedStateFlow.value)
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
            coroutineContext = coroutineContext
        )
        assertEquals(factoryValue, krate.cachedStateFlow.value)
        assertEquals(loaderValue, krate.getValue())
        assertEquals(loaderValue, krate.cachedStateFlow.value)
    }

    @Test
    fun GIVEN_empty_store_WHEN_save_and_reset_THEN_saved_and_reset() = runTest {
        val factoryValue = 10
        val store = MapSettings()
        val krate = DefaultStateFlowSuspendMutableKrate(
            factory = { factoryValue },
            saver = { store.putInt("KEY", it) },
            loader = { store.getIntOrNull("KEY") },
            coroutineContext = coroutineContext
        )
        assertEquals(factoryValue, krate.cachedStateFlow.value)
        assertEquals(factoryValue, krate.getValue())
        11.let { newValue ->
            krate.save(newValue)
            assertEquals(newValue, krate.cachedStateFlow.value)
            assertEquals(newValue, krate.getValue())
        }
        krate.reset()
        assertEquals(factoryValue, krate.cachedStateFlow.value)
        assertEquals(factoryValue, krate.getValue())
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
            coroutineContext = coroutineContext
        )
        assertEquals(factoryValue, krate.cachedStateFlow.value)
        assertEquals(defaultStoreValue, krate.getValue())
        assertEquals(defaultStoreValue, krate.cachedStateFlow.value)
        11.let { newValue ->
            krate.save(newValue)
            assertEquals(newValue, krate.cachedStateFlow.value)
            assertEquals(newValue, krate.getValue())
        }
        store.remove("KEY")
        krate.reset()
        assertEquals(factoryValue, krate.cachedStateFlow.value)
        assertEquals(factoryValue, krate.getValue())
    }

    @Test
    fun GIVEN_manually_not_loaded_WHEN_getting_cached_THEN_loaded_not_factory() = runTest {
        val factoryValue = 10
        val loadedValue = 30
        val krate = DefaultStateFlowSuspendMutableKrate(
            factory = { factoryValue },
            saver = { },
            loader = { loadedValue },
            coroutineContext = coroutineContext
        )

        assertEquals(
            expected = loadedValue,
            actual = krate.cachedStateFlow
                .filter { value -> value == loadedValue }
                .timeout(1.seconds)
                .first()
        )
    }
}
