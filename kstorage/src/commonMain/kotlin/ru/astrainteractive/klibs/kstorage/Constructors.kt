@file:Suppress("FunctionNaming")

package ru.astrainteractive.klibs.kstorage

import ru.astrainteractive.klibs.kstorage.api.MutableStorageValue
import ru.astrainteractive.klibs.kstorage.api.StateFlowMutableStorageValue
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import ru.astrainteractive.klibs.kstorage.api.value.ValueLoader
import ru.astrainteractive.klibs.kstorage.api.value.ValueSaver
import ru.astrainteractive.klibs.kstorage.impl.MutableStorageValueImpl
import ru.astrainteractive.klibs.kstorage.impl.StateFlowMutableStorageValueImpl

fun <T> MutableStorageValue(
    factory: ValueFactory<T>,
    saver: ValueSaver<T>,
    loader: ValueLoader<T>,
): MutableStorageValue<T> {
    return MutableStorageValueImpl(
        factory = factory,
        loader = loader,
        saver = saver
    )
}

fun <T> StateFlowMutableStorageValue(
    factory: ValueFactory<T>,
    saver: ValueSaver<T>,
    loader: ValueLoader<T>,
): StateFlowMutableStorageValue<T> {
    return StateFlowMutableStorageValueImpl(
        factory = factory,
        loader = loader,
        saver = saver
    )
}

fun <T> StateFlowMutableStorageValue(
    factory: ValueFactory<T>,
): StateFlowMutableStorageValue<T> {
    return StateFlowMutableStorageValueImpl(
        factory = factory,
        loader = { factory.create() },
        saver = {}
    )
}
