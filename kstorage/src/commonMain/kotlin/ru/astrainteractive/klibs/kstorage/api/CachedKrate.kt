package ru.astrainteractive.klibs.kstorage.api

interface CachedKrate<T> : Krate<T> {
    val cachedValue: T
}
