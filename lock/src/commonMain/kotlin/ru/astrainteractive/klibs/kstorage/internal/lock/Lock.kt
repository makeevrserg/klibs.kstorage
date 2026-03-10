package ru.astrainteractive.klibs.kstorage.internal.lock

interface Lock {
    fun <T> withLock(block: () -> T): T
}
