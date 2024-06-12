package ru.astrainteractive.klibs.kstorage.util

import kotlinx.coroutines.flow.first
import ru.astrainteractive.klibs.kstorage.api.MutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.FlowMutableKrate

object KrateExt {
    /**
     * This will call [MutableKrate.reset] and return [MutableKrate.cachedValue]
     */
    fun <T> MutableKrate<T>.resetAndGet(): T {
        reset()
        return cachedValue
    }

    /**
     * Save value with a reference to current
     */
    fun <T> MutableKrate<T>.update(block: (T) -> T) {
        save(block.invoke(cachedValue))
    }

    /**
     * Save value with a reference to current and return new value
     */
    fun <T> MutableKrate<T>.updateAndGet(block: (T) -> T): T {
        val oldValue = loadAndGet()
        val newValue = block.invoke(oldValue)
        save(newValue)
        return newValue
    }

    /**
     * This will call [FlowMutableKrate.reset] and return first [FlowMutableKrate.flow]
     */
    suspend fun <T> FlowMutableKrate<T>.resetAndGet(): T {
        reset()
        return flow.first()
    }

    /**
     * Save value with a reference to current
     */
    suspend fun <T> FlowMutableKrate<T>.update(block: suspend (T) -> T) {
        val oldValue = flow.first()
        val newValue = block.invoke(oldValue)
        save(newValue)
    }

    /**
     * Save value with a reference to current and return new value
     */
    suspend fun <T> FlowMutableKrate<T>.updateAndGet(block: suspend (T) -> T): T {
        val oldValue = flow.first()
        val newValue = block.invoke(oldValue)
        save(newValue)
        return newValue
    }
}
