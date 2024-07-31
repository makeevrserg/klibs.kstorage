package ru.astrainteractive.klibs.kstorage.suspend.value

import kotlinx.coroutines.flow.Flow

fun interface FlowProvider<out T> {
    fun provide(): Flow<T?>
}
