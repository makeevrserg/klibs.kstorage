package ru.astrainteractive.klibs.kstorage

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.timeout
import kotlinx.coroutines.test.runTest
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultSuspendMutableKrate
import ru.astrainteractive.klibs.kstorage.test.SampleStore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

/**
 * Test cases:
 * 1. Factory value not null, loader is null.
 * 2. Factory value is null, loader not null
 * 3. Factory value is one, the loader is another
 * 4. Factory value is null,m the loader is null
 */
internal class SuspendMutableKrateTest {
    @Test
    fun GIVEN_10_as_default_value_and_loader_null_WHEN_load_THEN_return_default() = runTest {
        val factoryValue = 10
        val store = SampleStore()
        val krate = DefaultSuspendMutableKrate(
            factory = { factoryValue },
            saver = { store.put("KEY", it) },
            loader = { null },
            coroutineContext = coroutineContext
        )
        assertEquals(factoryValue, krate.cachedValue)
        assertEquals(factoryValue, krate.loadAndGet())
        assertEquals(factoryValue, krate.cachedValue)
    }

    @Test
    fun GIVEN_null_as_default_10_as_loader_WHEN_load_THEN_return_loader() = runTest {
        val loaderValue = 10
        val store = SampleStore()
        val krate = DefaultSuspendMutableKrate(
            factory = { null },
            saver = { store.put("KEY", it) },
            loader = { loaderValue },
            coroutineContext = coroutineContext
        )
        assertEquals(null, krate.cachedValue)
        assertEquals(loaderValue, krate.loadAndGet())
        assertEquals(loaderValue, krate.cachedValue)
    }

    @Test
    fun GIVEN_one_as_default_another_as_loader_WHEN_load_THEN_return_loader() = runTest {
        val loaderValue = 10
        val factoryValue = 15
        val store = SampleStore()
        val krate = DefaultSuspendMutableKrate(
            factory = { factoryValue },
            saver = { store.put("KEY", it) },
            loader = { loaderValue },
            coroutineContext = coroutineContext
        )
        assertEquals(factoryValue, krate.cachedValue)
        assertEquals(loaderValue, krate.loadAndGet())
        assertEquals(loaderValue, krate.cachedValue)
    }

    @Test
    fun GIVEN_empty_store_WHEN_save_and_reset_THEN_saved_and_reset() = runTest {
        val factoryValue = 10
        val store = SampleStore()
        val krate = DefaultSuspendMutableKrate(
            factory = { factoryValue },
            saver = { store.put("KEY", it) },
            loader = { store.get("KEY") },
            coroutineContext = coroutineContext
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
        val defaultStoreValue = 15
        val store = SampleStore(mapOf("KEY" to defaultStoreValue))
        val krate = DefaultSuspendMutableKrate(
            factory = { factoryValue },
            saver = { store.put("KEY", it) },
            loader = { store.get("KEY") },
            coroutineContext = coroutineContext
        )
        assertEquals(factoryValue, krate.cachedValue)
        assertEquals(defaultStoreValue, krate.loadAndGet())
        assertEquals(defaultStoreValue, krate.cachedValue)
        11.let { newValue ->
            krate.save(newValue)
            assertEquals(newValue, krate.cachedValue)
            assertEquals(newValue, krate.loadAndGet())
        }
        store.put("KEY", null)
        krate.reset()
        assertEquals(factoryValue, krate.cachedValue)
        assertEquals(factoryValue, krate.loadAndGet())
    }

    @Test
    fun GIVEN_manually_not_loaded_WHEN_getting_cached_THEN_loaded_not_factory() = runTest {
        val factoryValue = 10
        val loadedValue = 30
        val krate = DefaultSuspendMutableKrate(
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
