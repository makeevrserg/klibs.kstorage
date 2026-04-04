package ru.astrainteractive.klibs.kstorage.internal.lock

actual fun Lock(): Lock {
    return AppleLock()
}
