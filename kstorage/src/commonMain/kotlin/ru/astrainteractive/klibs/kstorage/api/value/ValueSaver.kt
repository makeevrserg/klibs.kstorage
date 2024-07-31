package ru.astrainteractive.klibs.kstorage.api.value

fun interface ValueSaver<T> {
    fun save(value: T)
    class Empty<T> : ValueSaver<T> {
        override fun save(value: T) = Unit
    }
}
