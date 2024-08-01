package ru.astrainteractive.klibs.kstorage.api.flow

import ru.astrainteractive.klibs.kstorage.api.MutableKrate

/**
 * [StateFlowKrate] allows you to use your storage values in reactive way
 */
interface StateFlowMutableKrate<T> : MutableKrate<T>, StateFlowKrate<T>
