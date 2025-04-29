package ru.astrainteractive.klibs.kstorage.api

/**
 * [Krate] is a wrapper for your favorite key-value storage library
 */
interface Krate<T> {

    fun getValue(): T
}
