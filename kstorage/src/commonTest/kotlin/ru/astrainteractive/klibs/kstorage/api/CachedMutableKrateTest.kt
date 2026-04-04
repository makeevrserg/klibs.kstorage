package ru.astrainteractive.klibs.kstorage.api

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.test.runTest
import ru.astrainteractive.klibs.kstorage.api.impl.DefaultMutableKrate
import ru.astrainteractive.klibs.kstorage.settings.MapSettings
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CachedMutableKrateTest {

    @Test
    fun GIVEN_10_as_default_value_and_loader_null_WHEN_load_THEN_return_default() =
        runTest {
            val factoryValue = 10
            val createKrate = {
                val store = MapSettings()
                DefaultMutableKrate(
                    factory = { factoryValue },
                    saver = { store.putInt("KEY", it) },
                    loader = { null }
                )
            }
            listOf(
                createKrate.invoke().asCachedKrate(),
                createKrate.invoke().asCachedMutableKrate(),
                createKrate.invoke().asStateFlowKrate(),
                createKrate.invoke().asStateFlowMutableKrate(),
            ).forEach { krate ->
                assertEquals(
                    expected = factoryValue,
                    actual = krate.cachedValue,
                    message = "Initial cachedValue should equal factory value when loader returns null"
                )
                assertEquals(
                    expected = factoryValue,
                    actual = krate.getValue(),
                    message = "getValue() should return factory value when loader returns null"
                )
                assertEquals(
                    expected = factoryValue,
                    actual = krate.cachedValue,
                    message = "cachedValue after getValue() should still equal factory value"
                )
            }
        }

    @Test
    fun GIVEN_null_as_default_10_as_loader_WHEN_load_THEN_return_loader() = runTest {
        val loaderValue = 10
        val createKrate = {
            val store = MapSettings()
            DefaultMutableKrate(
                factory = { null },
                saver = {
                    if (it == null) {
                        store.remove("KEY")
                    } else {
                        store.putInt("KEY", it)
                    }
                },
                loader = { loaderValue }
            )
        }
        listOf(
            createKrate.invoke().asCachedKrate(),
            createKrate.invoke().asCachedMutableKrate(),
            createKrate.invoke().asStateFlowKrate(),
            createKrate.invoke().asStateFlowMutableKrate(),
        ).forEach { krate ->
            assertEquals(
                expected = loaderValue,
                actual = krate.cachedValue,
                message = "Initial cachedValue should equal loader value when factory is null"
            )
            assertEquals(
                expected = loaderValue,
                actual = krate.getValue(),
                message = "getValue() should return loader value when factory is null"
            )
            assertEquals(
                expected = loaderValue,
                actual = krate.cachedValue,
                message = "cachedValue after getValue() should still equal loader value"
            )
        }
    }

    @Test
    fun GIVEN_one_as_default_another_as_loader_WHEN_load_THEN_return_loader() =
        runTest {
            val loaderValue = 10
            val factoryValue = 15
            val createKrate = {
                val store = MapSettings()
                DefaultMutableKrate(
                    factory = { factoryValue },
                    saver = { store.putInt("KEY", it) },
                    loader = { loaderValue }
                )
            }
            listOf(
                createKrate.invoke().asCachedKrate(),
                createKrate.invoke().asCachedMutableKrate(),
                createKrate.invoke().asStateFlowKrate(),
                createKrate.invoke().asStateFlowMutableKrate(),
            ).forEach { krate ->
                assertEquals(
                    expected = loaderValue,
                    actual = krate.cachedValue,
                    message = "cachedValue should equal loader value when both factory and loader are present"
                )
                assertEquals(
                    expected = loaderValue,
                    actual = krate.getValue(),
                    message = "getValue() should return loader value over factory value"
                )
                assertEquals(
                    expected = loaderValue,
                    actual = krate.cachedValue,
                    message = "cachedValue after getValue() should still equal loader value"
                )
            }
        }

    @Test
    fun GIVEN_empty_store_WHEN_save_and_reset_THEN_saved_and_reset() = runTest {
        val factoryValue = 10
        val createKrate = {
            val store = MapSettings()
            DefaultMutableKrate(
                factory = { factoryValue },
                saver = { store.putInt("KEY", it) },
                loader = { store.getIntOrNull("KEY") }
            ).asCachedMutableKrate()
        }
        listOf(
            createKrate.invoke().asCachedMutableKrate(),
            createKrate.invoke().asStateFlowMutableKrate(),
        ).forEach { krate ->
            assertEquals(
                expected = factoryValue,
                actual = krate.cachedValue,
                message = "Initial cachedValue should equal factory value for empty store"
            )
            assertEquals(
                expected = factoryValue,
                actual = krate.getValue(),
                message = "Initial getValue() should return factory value for empty store"
            )
            11.let { newValue ->
                krate.save(newValue)
                assertEquals(
                    expected = newValue,
                    actual = krate.cachedValue,
                    message = "cachedValue should equal new value after save"
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
                actual = krate.cachedValue,
                message = "cachedValue should return to factory value after reset"
            )
            assertEquals(
                expected = factoryValue,
                actual = krate.getValue(),
                message = "getValue() should return factory value after reset"
            )
        }
    }

    @Test
    fun GIVEN_prefilled_store_WHEN_save_and_reset_THEN_saved_and_reset() = runTest {
        val factoryValue = 10
        val defaultStoreValue = 15
        val store = MapSettings(mapOf("KEY" to defaultStoreValue))
        val createKrate = {
            DefaultMutableKrate(
                factory = { factoryValue },
                saver = { store.putInt("KEY", it) },
                loader = { store.getIntOrNull("KEY") }
            )
        }
        listOf(
            createKrate.invoke().asCachedMutableKrate(),
            createKrate.invoke().asStateFlowMutableKrate(),
        ).forEach { krate ->
            store.clear()
            store.putInt("KEY", defaultStoreValue)

            assertEquals(
                expected = defaultStoreValue,
                actual = krate.cachedValue,
                message = "Initial cachedValue should equal pre-filled store value"
            )
            assertEquals(
                expected = defaultStoreValue,
                actual = krate.getValue(),
                message = "Initial getValue() should return pre-filled store value"
            )
            11.let { newValue ->
                krate.save(newValue)
                assertEquals(
                    expected = newValue,
                    actual = krate.cachedValue,
                    message = "cachedValue should equal new value after save"
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
                actual = krate.cachedValue,
                message = "cachedValue should return to factory value after reset"
            )
            assertEquals(
                expected = factoryValue,
                actual = krate.getValue(),
                message = "getValue() should return factory value after reset"
            )
        }
    }

    @Test
    fun GIVEN_2prefilled_store_WHEN_save_and_reset_THEN_saved_and_reset() = runTest {
        val store = MapSettings()
        val createKrate = {
            DefaultMutableKrate(
                factory = { 0 },
                saver = { store.putInt("KEY", it) },
                loader = { store.getIntOrNull("KEY") }
            )
        }
        listOf(
            createKrate.invoke().asCachedMutableKrate(),
            createKrate.invoke().asStateFlowMutableKrate(),
        ).onEach { krate ->
            List(500) {
                async(start = CoroutineStart.LAZY) { krate.save { value -> value + 1 } }
            }.awaitAll()
        }.onEach { krate ->
            assertEquals(
                expected = 1000,
                actual = krate.getValue(),
                message = "After 500 concurrent increments per krate, total should be 1000"
            )
        }
    }
}
