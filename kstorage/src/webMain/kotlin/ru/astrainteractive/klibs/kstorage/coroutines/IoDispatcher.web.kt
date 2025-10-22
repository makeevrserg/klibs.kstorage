package ru.astrainteractive.klibs.kstorage.coroutines

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

internal actual fun getIoDispatcher(): CoroutineContext {
    return Dispatchers.Unconfined
}
