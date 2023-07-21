package ru.astrainteractive.klibs.kstorage.api

import kotlinx.coroutines.flow.StateFlow

/**
 * [StateFlowMutableStorageValue] allows you to use your storage values in reactive way
 */
interface StateFlowMutableStorageValue<T> : MutableStorageValue<T> {
    val stateFlow: StateFlow<T>

    override val value: T
        get() = stateFlow.value
}
