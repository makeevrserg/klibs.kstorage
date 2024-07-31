package ru.astrainteractive.klibs.kstorage.suspend.flow

import ru.astrainteractive.klibs.kstorage.suspend.SuspendMutableKrate

interface FlowMutableKrate<T> : FlowKrate<T>, SuspendMutableKrate<T>
