package ru.astrainteractive.klibs.kstorage.api

/**
 * [MutableStorageValue] allows you to save/load values from your storage without
 * depending on SharedPreferences or other library
 */
interface MutableStorageValue<T> : StorageValue<T> {
    /**
     * Save new value into storage and update current
     */
    fun save(value: T)

    /**
     * Reset value to to default
     */
    fun reset()
}
