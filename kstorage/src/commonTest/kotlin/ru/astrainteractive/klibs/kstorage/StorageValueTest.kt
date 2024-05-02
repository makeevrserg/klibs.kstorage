package ru.astrainteractive.klibs.kstorage

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class StorageValueTest {
    @Test
    fun testLoadStorageValue() {
        val expectedValue = 11
        val storageValue = StateFlowMutableStorageValue(
            factory = { 0 },
            loader = { expectedValue },
            saver = { }
        )
        assertEquals(expectedValue, storageValue.value)
        assertEquals(expectedValue, storageValue.stateFlow.value)
        storageValue.load()
        assertEquals(expectedValue, storageValue.value)
        assertEquals(expectedValue, storageValue.stateFlow.value)
    }

    @Test
    fun testResetStorageValue() {
        val expectedValue = 0
        val storageValue = StateFlowMutableStorageValue(
            factory = { expectedValue },
            loader = { 11 },
            saver = { }
        )
        assertNotEquals(expectedValue, storageValue.value)
        assertNotEquals(expectedValue, storageValue.stateFlow.value)
        storageValue.reset()
        assertEquals(expectedValue, storageValue.value)
        assertEquals(expectedValue, storageValue.stateFlow.value)
    }

    @Test
    fun testSaveStorageValue() {
        val storageValue = StateFlowMutableStorageValue(
            factory = { 0 },
            loader = { },
            saver = { }
        )
        storageValue.save(10)
        assertEquals(storageValue.value, 10)
        assertEquals(storageValue.stateFlow.value, 10)
    }

    @Test
    fun testInMemoryStorageValue() {
        val expectedValue = 0
        val storageValue = StateFlowMutableStorageValue { expectedValue }
        assertEquals(expectedValue, storageValue.value)
        assertEquals(expectedValue, storageValue.stateFlow.value)
        val newExpectedValue = expectedValue * 2
        assertEquals(newExpectedValue, storageValue.value)
        assertEquals(newExpectedValue, storageValue.stateFlow.value)
    }
}
