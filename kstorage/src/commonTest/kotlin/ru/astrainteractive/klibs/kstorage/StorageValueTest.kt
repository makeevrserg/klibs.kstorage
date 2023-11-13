package ru.astrainteractive.klibs.kstorage

import kotlin.test.Test
import kotlin.test.assertEquals

class StorageValueTest {
    @Test
    fun testLoadStorageValue() {
        val storageValue = StateFlowMutableStorageValue(
            default = 0,
            loadSettingsValue = { 11 },
            saveSettingsValue = {}
        )
        assertEquals(11, storageValue.value)
        assertEquals(11, storageValue.stateFlow.value)
        storageValue.load()
        assertEquals(11, storageValue.value)
        assertEquals(11, storageValue.stateFlow.value)
    }

    @Test
    fun testSaveStorageValue() {
        val storageValue = StateFlowMutableStorageValue(
            default = 0,
            loadSettingsValue = { },
            saveSettingsValue = {}
        )
        storageValue.save(10)
        assertEquals(storageValue.value, 10)
        assertEquals(storageValue.stateFlow.value, 10)
    }
}
