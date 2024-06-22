package ru.astrainteractive.klibs.kstorage.api

/**
 * [StateFlowMutableKrate] allows you to use your storage values in reactive way
 */
interface StateFlowMutableKrate<T> : MutableKrate<T>, StateFlowKrate<T>
