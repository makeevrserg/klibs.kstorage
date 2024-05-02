package ru.astrainteractive.klibs.kstorage

import ru.astrainteractive.klibs.kstorage.api.MutableStorageValue
import ru.astrainteractive.klibs.kstorage.api.StateFlowMutableStorageValue
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory

/**
 * [withDefault] is used to convert nullable to non nullable
 */
fun <T : Any> MutableStorageValue<T?>.withDefault(factory: ValueFactory<T>): MutableStorageValue<T> {
    return MutableStorageValue(
        factory = factory,
        saver = { value -> save(value) },
        loader = { load() ?: factory.create() }
    )
}

/**
 * [withDefault] is used to convert nullable to non nullable
 */
fun <T : Any> StateFlowMutableStorageValue<T?>.withDefault(factory: ValueFactory<T>): StateFlowMutableStorageValue<T> {
    return StateFlowMutableStorageValue(
        factory = factory,
        saver = { value -> save(value) },
        loader = { load() ?: factory.create() }
    )
}

/**
 * Save value with a reference to current
 */
fun <T> MutableStorageValue<T>.update(block: (T) -> T) {
    save(block.invoke(value))
}
