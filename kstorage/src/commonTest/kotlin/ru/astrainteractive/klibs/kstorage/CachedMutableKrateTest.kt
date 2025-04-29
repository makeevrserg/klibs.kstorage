package ru.astrainteractive.klibs.kstorage

import kotlinx.coroutines.test.runTest
import ru.astrainteractive.klibs.kstorage.api.impl.DefaultMutableKrate
import ru.astrainteractive.klibs.kstorage.test.SampleStore
import ru.astrainteractive.klibs.kstorage.util.asCachedKrate
import ru.astrainteractive.klibs.kstorage.util.asCachedMutableKrate
import ru.astrainteractive.klibs.kstorage.util.asStateFlowKrate
import ru.astrainteractive.klibs.kstorage.util.asStateFlowMutableKrate
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CachedMutableKrateTest {
    @Test
    fun GIVEN_10_as_default_value_and_loader_null_WHEN_load_THEN_return_default() = runTest {
        val factoryValue = 10
        val createKrate = {
            val store = SampleStore()
            DefaultMutableKrate(
                factory = { factoryValue },
                saver = { store.put("KEY", it) },
                loader = { null }
            )
        }
        listOf(
            createKrate.invoke().asCachedKrate(),
            createKrate.invoke().asCachedMutableKrate(),
            createKrate.invoke().asStateFlowKrate(),
            createKrate.invoke().asStateFlowMutableKrate(),
        ).forEach { krate ->
            assertEquals(factoryValue, krate.cachedValue)
            assertEquals(factoryValue, krate.getValue())
            assertEquals(factoryValue, krate.cachedValue)
        }
    }

    @Test
    fun GIVEN_null_as_default_10_as_loader_WHEN_load_THEN_return_loader() = runTest {
        val loaderValue = 10
        val createKrate = {
            val store = SampleStore()
            DefaultMutableKrate(
                factory = { null },
                saver = { store.put("KEY", it) },
                loader = { loaderValue }
            )
        }
        listOf(
            createKrate.invoke().asCachedKrate(),
            createKrate.invoke().asCachedMutableKrate(),
            createKrate.invoke().asStateFlowKrate(),
            createKrate.invoke().asStateFlowMutableKrate(),
        ).forEach { krate ->
            assertEquals(loaderValue, krate.cachedValue)
            assertEquals(loaderValue, krate.getValue())
            assertEquals(loaderValue, krate.cachedValue)
        }
    }

    @Test
    fun GIVEN_one_as_default_another_as_loader_WHEN_load_THEN_return_loader() = runTest {
        val loaderValue = 10
        val factoryValue = 15
        val createKrate = {
            val store = SampleStore()
            DefaultMutableKrate(
                factory = { factoryValue },
                saver = { store.put("KEY", it) },
                loader = { loaderValue }
            )
        }
        listOf(
            createKrate.invoke().asCachedKrate(),
            createKrate.invoke().asCachedMutableKrate(),
            createKrate.invoke().asStateFlowKrate(),
            createKrate.invoke().asStateFlowMutableKrate(),
        ).forEach { krate ->
            assertEquals(loaderValue, krate.cachedValue)
            assertEquals(loaderValue, krate.getValue())
            assertEquals(loaderValue, krate.cachedValue)
        }
    }

    @Test
    fun GIVEN_empty_store_WHEN_save_and_reset_THEN_saved_and_reset() = runTest {
        val factoryValue = 10
        val createKrate = {
            val store = SampleStore()
            DefaultMutableKrate(
                factory = { factoryValue },
                saver = { store.put("KEY", it) },
                loader = { store.get("KEY") }
            )
        }
        listOf(
            createKrate.invoke().asCachedMutableKrate(),
            createKrate.invoke().asStateFlowMutableKrate(),
        ).forEach { krate ->
            assertEquals(factoryValue, krate.cachedValue)
            assertEquals(factoryValue, krate.getValue())
            11.let { newValue ->
                krate.save(newValue)
                assertEquals(newValue, krate.cachedValue)
                assertEquals(newValue, krate.getValue())
            }
            krate.reset()
            assertEquals(factoryValue, krate.cachedValue)
            assertEquals(factoryValue, krate.getValue())
        }
    }

    @Test
    fun GIVEN_prefilled_store_WHEN_save_and_reset_THEN_saved_and_reset() = runTest {
        val factoryValue = 10
        val defaultStoreValue = 15
        val store = SampleStore(mapOf("KEY" to defaultStoreValue))
        val createKrate = {
            DefaultMutableKrate(
                factory = { factoryValue },
                saver = { store.put("KEY", it) },
                loader = { store.get("KEY") }
            )
        }
        listOf(
            createKrate.invoke().asCachedMutableKrate(),
            createKrate.invoke().asStateFlowMutableKrate(),
        ).forEach { krate ->
            store.clear()
            store.put("KEY", defaultStoreValue)

            assertEquals(defaultStoreValue, krate.cachedValue)
            assertEquals(defaultStoreValue, krate.getValue())
            11.let { newValue ->
                krate.save(newValue)
                assertEquals(newValue, krate.cachedValue)
                assertEquals(newValue, krate.getValue())
            }
            store.put("KEY", null)
            krate.reset()
            assertEquals(factoryValue, krate.cachedValue)
            assertEquals(factoryValue, krate.getValue())
        }
    }
}
