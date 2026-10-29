package ru.astrainteractive.klibs.kstorage.lock.api

@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "The lock API is shared with kstorage and is not meant for other consumers"
)
@Retention(AnnotationRetention.BINARY)
public annotation class InternalLockApi
