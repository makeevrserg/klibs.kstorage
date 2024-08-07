package ru.astrainteractive.klibs.kstorage.suspend.flow

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import ru.astrainteractive.klibs.kstorage.suspend.SuspendKrate

interface FlowKrate<T> : SuspendKrate<T> {
    val flow: Flow<T>

    fun stateFlow(
        coroutineScope: CoroutineScope,
        sharingStarted: SharingStarted = SharingStarted.Eagerly,
        dispatcher: CoroutineDispatcher = Dispatchers.Unconfined
    ): StateFlow<T>

    val CoroutineScope.stateFlow: StateFlow<T>
        get() = stateFlow(coroutineScope = this)
}
