package ru.astrainteractive.klibs.kstorage.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

internal actual fun getIoDispatcher(): kotlin.coroutines.CoroutineContext {
    return Dispatchers.IO
}
