package ru.astrainteractive.klibs.kstorage.util

import ru.astrainteractive.klibs.kstorage.api.CachedKrate
import ru.astrainteractive.klibs.kstorage.api.Krate
import ru.astrainteractive.klibs.kstorage.suspend.SuspendKrate

object KrateExt {
    /**
     * This will call [Krate.Mutable] and return [CachedKrate.cachedValue]
     */
    fun <T> Krate.Mutable<T>.resetAndGet(): T {
        reset()
        return cachedValue
    }

    /**
     * Save value with a reference to current
     */
    fun <T> Krate.Mutable<T>.update(block: (T) -> T) {
        save(block.invoke(cachedValue))
    }

    /**
     * Save value with a reference to current and return new value
     */
    fun <T> Krate.Mutable<T>.updateAndGet(block: (T) -> T): T {
        val oldValue = loadAndGet()
        val newValue = block.invoke(oldValue)
        save(newValue)
        return newValue
    }

    /**
     * This will call [SuspendKrate.Mutable.reset] and return first [SuspendKrate.loadAndGet]
     */
    suspend fun <T> SuspendKrate.Mutable<T>.resetAndGet(): T {
        reset()
        return loadAndGet()
    }

    /**
     * Save value with a reference to current
     */
    suspend fun <T> SuspendKrate.Mutable<T>.update(block: suspend (T) -> T) {
        val oldValue = loadAndGet()
        val newValue = block.invoke(oldValue)
        save(newValue)
    }

    /**
     * Save value with a reference to current and return new value
     */
    suspend fun <T> SuspendKrate.Mutable<T>.updateAndGet(block: suspend (T) -> T): T {
        val oldValue = loadAndGet()
        val newValue = block.invoke(oldValue)
        save(newValue)
        return newValue
    }
}
