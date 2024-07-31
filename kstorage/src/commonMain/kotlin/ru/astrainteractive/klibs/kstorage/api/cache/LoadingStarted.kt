package ru.astrainteractive.klibs.kstorage.api.cache

import ru.astrainteractive.klibs.kstorage.api.Krate
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import ru.astrainteractive.klibs.kstorage.api.value.ValueLoader

/**
 * A strategy for load initial value for [CacheOwner]
 */
enum class LoadingStarted {
    /**
     * Loading is started immediately from [ValueLoader] and then [ValueFactory]
     */
    Instantly,

    /**
     * Loading is started from [ValueFactory] and developer should manually call [Krate.loadAndGet] to load value
     */
    Manually
}
