package ru.astrainteractive.klibs.kstorage.test

import kotlin.test.Test
import kotlin.test.assertEquals

internal class SampleStoreTest {
    private fun <T : Any> SampleStore.assertEqualsStored(key: String, value: T) {
        put(key, value)
        assertEquals(value, get(key))
    }

    @Test
    fun GIVEN_value_WHEN_save_and_check_get_THEN_success() {
        val store = SampleStore()
        store.assertEqualsStored("int", 10)
        store.assertEqualsStored("string", "hello world")
    }
}
