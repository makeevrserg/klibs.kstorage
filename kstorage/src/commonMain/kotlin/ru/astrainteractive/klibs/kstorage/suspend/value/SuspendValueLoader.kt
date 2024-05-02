package ru.astrainteractive.klibs.kstorage.suspend.value

import kotlinx.coroutines.flow.Flow

fun interface SuspendValueLoader<out T> {
    fun getFlow(): Flow<T>
}
