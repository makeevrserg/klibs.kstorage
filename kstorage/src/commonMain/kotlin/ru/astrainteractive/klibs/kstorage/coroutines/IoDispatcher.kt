package ru.astrainteractive.klibs.kstorage.coroutines

import kotlin.coroutines.CoroutineContext

internal expect fun getIoDispatcher(): CoroutineContext
