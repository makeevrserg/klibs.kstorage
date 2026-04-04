package ru.astrainteractive.klibs.kstorage.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal actual fun getIoDispatcher(): CoroutineDispatcher {
    return Dispatchers.Default
}
