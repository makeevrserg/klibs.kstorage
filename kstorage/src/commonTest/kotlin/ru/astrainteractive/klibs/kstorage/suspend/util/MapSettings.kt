package ru.astrainteractive.klibs.kstorage.suspend.util

import com.russhwolf.settings.Settings

internal class MapSettings : Settings {
    private val map = mutableMapOf<Any, Any>()
    override fun clear() {
        map.clear()
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return map.getOrElse(key) { defaultValue } as Boolean
    }

    override fun getBooleanOrNull(key: String): Boolean? {
        return map[key] as? Boolean?
    }

    override fun getDouble(key: String, defaultValue: Double): Double {
        return map.getOrElse(key) { defaultValue } as Double
    }

    override fun getDoubleOrNull(key: String): Double? {
        return map[key] as? Double?
    }

    override fun getFloat(key: String, defaultValue: Float): Float {
        return map.getOrElse(key) { defaultValue } as Float
    }

    override fun getFloatOrNull(key: String): Float? {
        return map[key] as? Float?
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return map.getOrElse(key) { defaultValue } as Int
    }

    override fun getIntOrNull(key: String): Int? {
        return map[key] as? Int?
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        return map.getOrElse(key) { defaultValue } as Long
    }

    override fun getLongOrNull(key: String): Long? {
        return map[key] as? Long?
    }

    override fun getString(key: String, defaultValue: String): String {
        return map.getOrElse(key) { defaultValue } as String
    }

    override fun getStringOrNull(key: String): String? {
        return map[key] as? String?
    }

    override fun hasKey(key: String): Boolean {
        return key in map
    }

    override fun putBoolean(key: String, value: Boolean) {
        map.put(key, value)
    }

    override fun putDouble(key: String, value: Double) {
        map.put(key, value)
    }

    override fun putFloat(key: String, value: Float) {
        map.put(key, value)
    }

    override fun putInt(key: String, value: Int) {
        map.put(key, value)
    }

    override fun putLong(key: String, value: Long) {
        map.put(key, value)
    }

    override fun putString(key: String, value: String) {
        map.put(key, value)
    }

    override fun remove(key: String) {
        map.remove(key)
    }

    override val keys: Set<String>
        get() = map.keys.map(Any::toString).toSet()
    override val size: Int
        get() = map.size
}
