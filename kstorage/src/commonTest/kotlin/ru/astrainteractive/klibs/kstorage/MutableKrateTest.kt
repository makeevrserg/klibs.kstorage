package ru.astrainteractive.klibs.kstorage

import ru.astrainteractive.klibs.kstorage.test.StoreMutableKrate
import ru.astrainteractive.klibs.kstorage.util.KrateDefaultExt.withDefault
import kotlin.test.Test
import kotlin.test.assertEquals

internal class MutableKrateTest {
    @Test
    fun GIVEN_10_as_default_value_and_loader_null_WHEN_load_THEN_return_default() {
        val expectValue = 10
        val krate = StoreMutableKrate(factory = { expectValue })
        assertEquals(expectValue, krate.cachedValue)
        assertEquals(expectValue, krate.loadAndGet())
    }

    @Test
    fun GIVEN_10_as_loader_value_and_default_null_WHEN_load_THEN_return_loader() {
        val defaultValue = 10
        val krate = StoreMutableKrate(factory = { defaultValue })
        assertEquals(defaultValue, krate.cachedValue)
        assertEquals(defaultValue, krate.loadAndGet())
    }

    @Test
    fun GIVEN_saved_and_resed_WHEN_save_and_reset_THEN_saved_and_reset() {
        val defaultValue = 10
        val krate = StoreMutableKrate(factory = { defaultValue })
        11.let { newValue ->
            krate.save(newValue)
            assertEquals(newValue, krate.cachedValue)
            assertEquals(newValue, krate.loadAndGet())
        }
        krate.reset()
        assertEquals(defaultValue, krate.cachedValue)
        assertEquals(defaultValue, krate.loadAndGet())
    }

    @Test
    fun GIVEN_null_then_with_default_WHEN_get_THEN_default() {
        val defaultValue = 10
        val krate = StoreMutableKrate<Int?>(factory = { null })
            .withDefault(factory = { defaultValue })
        assertEquals(defaultValue, krate.cachedValue)
        assertEquals(defaultValue, krate.loadAndGet())
    }
}
