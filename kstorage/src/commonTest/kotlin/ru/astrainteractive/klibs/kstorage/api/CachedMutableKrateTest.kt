package ru.astrainteractive.klibs.kstorage.api

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.test.TestResult
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import ru.astrainteractive.klibs.kstorage.api.impl.DefaultMutableKrate
import ru.astrainteractive.klibs.kstorage.settings.MapSettings
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.measureTime

internal class CachedMutableKrateTest {

    fun runTestWithDuration(
        tag: String,
        testBody: suspend TestScope.() -> Unit
    ): TestResult {
        val result: TestResult
        val time = measureTime {
            result = runTest(testBody = {
                withContext(Dispatchers.Default) {
                    testBody()
                }
            })
        }
        println("$tag: EXECUTED TIME: $time")
        return result
    }

    @Test
    fun GIVEN_10_as_default_value_and_loader_null_WHEN_load_THEN_return_default() =
        runTestWithDuration("1") {
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
                assertEquals(factoryValue, krate.cachedValue)
                assertEquals(factoryValue, krate.getValue())
                assertEquals(factoryValue, krate.cachedValue)
            }
        }

    @Test
    fun GIVEN_null_as_default_10_as_loader_WHEN_load_THEN_return_loader() = runTestWithDuration("2") {
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
            assertEquals(loaderValue, krate.cachedValue)
            assertEquals(loaderValue, krate.getValue())
            assertEquals(loaderValue, krate.cachedValue)
        }
    }

    @Test
    fun GIVEN_one_as_default_another_as_loader_WHEN_load_THEN_return_loader() =
        runTestWithDuration("3") {
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
                assertEquals(loaderValue, krate.cachedValue)
                assertEquals(loaderValue, krate.getValue())
                assertEquals(loaderValue, krate.cachedValue)
            }
        }

    @Test
    fun GIVEN_empty_store_WHEN_save_and_reset_THEN_saved_and_reset() = runTestWithDuration("4") {
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
    fun GIVEN_prefilled_store_WHEN_save_and_reset_THEN_saved_and_reset() = runTestWithDuration("5") {
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

            assertEquals(defaultStoreValue, krate.cachedValue)
            assertEquals(defaultStoreValue, krate.getValue())
            11.let { newValue ->
                krate.save(newValue)
                assertEquals(newValue, krate.cachedValue)
                assertEquals(newValue, krate.getValue())
            }
            store.remove("KEY")
            krate.reset()
            assertEquals(factoryValue, krate.cachedValue)
            assertEquals(factoryValue, krate.getValue())
        }
    }

    @Test
    fun GIVEN_2prefilled_store_WHEN_save_and_reset_THEN_saved_and_reset() = runTestWithDuration("6") {
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
                async(
                    Dispatchers.Default,
                    start = CoroutineStart.LAZY
                ) { krate.save { value -> value + 1 } }
            }.awaitAll()
        }.onEach { krate ->
            assertEquals(1000, krate.getValue())
        }
    }
}
