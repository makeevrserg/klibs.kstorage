package ru.astrainteractive.klibs.kstorage

import ru.astrainteractive.klibs.kstorage.test.SampleStore
import ru.astrainteractive.klibs.kstorage.test.StoreStateFlowMutableKrate
import ru.astrainteractive.klibs.kstorage.util.KrateDefaultExt.withDefault
import kotlin.test.Test
import kotlin.test.assertEquals

internal class StateFlowMutableKrateTest {

    @Test
    fun GIVEN_10_as_default_value_and_loader_null_WHEN_load_THEN_return_default() {
        val expectValue = 10
        val krate = StoreStateFlowMutableKrate(
            factory = { expectValue }
        )
        assertEquals(expectValue, krate.cachedValue)
        assertEquals(expectValue, krate.loadAndGet())
        assertEquals(expectValue, krate.cachedStateFlow.value)
    }

    @Test
    fun GIVEN_10_as_loader_value_and_default_null_WHEN_load_THEN_return_loader() {
        val expectValue = 10
        val krate = StoreStateFlowMutableKrate<Int?>(
            factory = { null },
            key = "key",
            store = SampleStore(mapOf("key" to expectValue))
        )
        assertEquals(expectValue, krate.cachedValue)
        assertEquals(expectValue, krate.loadAndGet())
        assertEquals(expectValue, krate.cachedStateFlow.value)
    }

    @Test
    fun GIVEN_null_then_with_default_WHEN_get_THEN_default() {
        val defaultValue = 10
        val krate = StoreStateFlowMutableKrate<Int?>(factory = { null })
            .withDefault(factory = { defaultValue })
        assertEquals(defaultValue, krate.cachedValue)
        assertEquals(defaultValue, krate.loadAndGet())
        assertEquals(defaultValue, krate.cachedStateFlow.value)
    }
}
