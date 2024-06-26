package ru.astrainteractive.klibs.kstorage

import kotlinx.coroutines.test.runTest
import ru.astrainteractive.klibs.kstorage.api.impl.DefaultMutableKrate
import ru.astrainteractive.klibs.kstorage.test.SampleStore
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test cases:
 * 1. Factory value not null, loader is null.
 * 2. Factory value is null, loader not null
 * 3. Factory value is one, the loader is another
 * 4. Factory value is null,m the loader is null
 */
internal class MutableKrateTest {
    @Test
    fun GIVEN_10_as_default_value_and_loader_null_WHEN_load_THEN_return_default() = runTest {
        val factoryValue = 10
        val store = SampleStore()
        val krate = DefaultMutableKrate(
            factory = { factoryValue },
            saver = { store.put("KEY", it) },
            loader = { null }
        )
        assertEquals(factoryValue, krate.cachedValue)
        assertEquals(factoryValue, krate.loadAndGet())
        assertEquals(factoryValue, krate.cachedValue)
    }

    @Test
    fun GIVEN_null_as_default_10_as_loader_WHEN_load_THEN_return_loader() = runTest {
        val loaderValue = 10
        val store = SampleStore()
        val krate = DefaultMutableKrate(
            factory = { null },
            saver = { store.put("KEY", it) },
            loader = { loaderValue }
        )
        assertEquals(loaderValue, krate.cachedValue)
        assertEquals(loaderValue, krate.loadAndGet())
        assertEquals(loaderValue, krate.cachedValue)
    }

    @Test
    fun GIVEN_one_as_default_another_as_loader_WHEN_load_THEN_return_loader() = runTest {
        val loaderValue = 10
        val factoryValue = 15
        val store = SampleStore()
        val krate = DefaultMutableKrate(
            factory = { factoryValue },
            saver = { store.put("KEY", it) },
            loader = { loaderValue }
        )
        assertEquals(loaderValue, krate.cachedValue)
        assertEquals(loaderValue, krate.loadAndGet())
        assertEquals(loaderValue, krate.cachedValue)
    }

    @Test
    fun GIVEN_empty_store_WHEN_save_and_reset_THEN_saved_and_reset() = runTest {
        val factoryValue = 10
        val store = SampleStore()
        val krate = DefaultMutableKrate(
            factory = { factoryValue },
            saver = { store.put("KEY", it) },
            loader = { store.get("KEY") }
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
        val krate = DefaultMutableKrate(
            factory = { factoryValue },
            saver = { store.put("KEY", it) },
            loader = { store.get("KEY") }
        )
        assertEquals(defaultStoreValue, krate.cachedValue)
        assertEquals(defaultStoreValue, krate.loadAndGet())
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
}
