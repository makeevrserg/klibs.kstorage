package ru.astrainteractive.klibs.kstorage.suspend.provider

import kotlinx.coroutines.flow.Flow

fun interface FlowProvider<out T> {
    fun provide(): Flow<T?>
}
