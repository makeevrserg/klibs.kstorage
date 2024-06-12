package ru.astrainteractive.klibs.kstorage.api.provider

fun interface DefaultValueFactory<out T> {
    fun create(): T
}
