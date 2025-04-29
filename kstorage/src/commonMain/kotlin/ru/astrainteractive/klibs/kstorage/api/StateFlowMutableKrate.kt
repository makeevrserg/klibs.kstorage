package ru.astrainteractive.klibs.kstorage.api

/**
 * [StateFlowKrate] allows you to use your storage values in reactive way
 */
interface StateFlowMutableKrate<T> : CachedMutableKrate<T>, StateFlowKrate<T>
