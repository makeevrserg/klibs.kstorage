package ru.astrainteractive.klibs.kstorage.suspend.impl

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import ru.astrainteractive.klibs.kstorage.suspend.FlowMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.StateFlowSuspendMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.SuspendMutableKrate

class FlowToStateFlowSuspendMutableKrate<T>(
    private val instance: FlowMutableKrate<T>,
    scope: CoroutineScope
) : StateFlowSuspendMutableKrate<T>,
    SuspendMutableKrate<T> by instance {
    override val cachedStateFlow: StateFlow<T> = instance.stateFlow(scope)
}
