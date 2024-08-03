package ru.astrainteractive.klibs.kstorage.suspend.value

import kotlinx.coroutines.flow.Flow

/**
 * [FlowProvider] should provide you with flow. Use android.DataStore, for example
 */
fun interface FlowProvider<out T> {
    fun provide(): Flow<T?>
}
