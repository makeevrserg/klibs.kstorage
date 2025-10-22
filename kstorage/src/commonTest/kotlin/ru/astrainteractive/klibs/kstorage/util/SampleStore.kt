package ru.astrainteractive.klibs.kstorage.util

internal class SampleStore(filledValues: Map<String, Any?> = emptyMap()) {
    private val map: MutableMap<String, Any?> = HashMap()
    fun put(key: String, value: Any?) {
        map[key] = value
    }

    fun <T> get(key: String): T? {
        return map[key] as? T
    }

    fun clear() {
        map.clear()
    }

    init {
        map.putAll(filledValues)
    }
}
