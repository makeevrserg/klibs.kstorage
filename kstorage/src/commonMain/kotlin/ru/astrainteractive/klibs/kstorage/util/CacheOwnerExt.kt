package ru.astrainteractive.klibs.kstorage.util

import ru.astrainteractive.klibs.kstorage.api.cache.CacheOwner
import kotlin.reflect.KProperty

object CacheOwnerExt {
    operator fun <T> CacheOwner<T>.getValue(default: Any, property: KProperty<*>): T {
        return cachedValue
    }
}
