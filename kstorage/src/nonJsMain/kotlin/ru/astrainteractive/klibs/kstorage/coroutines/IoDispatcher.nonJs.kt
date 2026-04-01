package ru.astrainteractive.klibs.kstorage.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

internal actual fun getIoDispatcher(): CoroutineDispatcher {
    return Dispatchers.IO
}
