package ru.astrainteractive.klibs.kstorage.suspend

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow

interface FlowMutableKrate<T> : FlowKrate<T>, SuspendMutableKrate<T> {
    fun stateFlow(
        coroutineScope: CoroutineScope,
        sharingStarted: SharingStarted = SharingStarted.Eagerly,
        dispatcher: CoroutineDispatcher = Dispatchers.Unconfined
    ): StateFlow<T>
}
