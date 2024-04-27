package ru.astrainteractive.klibs.kstorage.api.value

fun interface ValueSaver<T> {
    fun save(value: T)
}
