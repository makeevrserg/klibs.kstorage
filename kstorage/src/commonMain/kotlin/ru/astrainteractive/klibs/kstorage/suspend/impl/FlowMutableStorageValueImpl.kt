package ru.astrainteractive.klibs.kstorage.suspend.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import ru.astrainteractive.klibs.kstorage.suspend.FlowMutableStorageValue
import ru.astrainteractive.klibs.kstorage.suspend.value.SuspendValueFactory
import ru.astrainteractive.klibs.kstorage.suspend.value.SuspendValueLoader
import ru.astrainteractive.klibs.kstorage.suspend.value.SuspendValueSaver

internal class FlowMutableStorageValueImpl<T>(
    private val factory: SuspendValueFactory<T>,
    private val loader: SuspendValueLoader<T>,
    private val saver: SuspendValueSaver<T>
) : FlowMutableStorageValue<T> {

    override val flow: Flow<T> = loader.getFlow()

    override suspend fun load(): T {
        return loader.getFlow().first()
    }

    override suspend fun save(value: T) {
        saver.save(value)
    }

    override suspend fun reset() {
        save(factory.create())
    }
}
