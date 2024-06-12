package ru.astrainteractive.klibs.kstorage.util

import kotlinx.coroutines.flow.map
import ru.astrainteractive.klibs.kstorage.api.Krate
import ru.astrainteractive.klibs.kstorage.api.MutableKrate
import ru.astrainteractive.klibs.kstorage.api.StateFlowMutableKrate
import ru.astrainteractive.klibs.kstorage.api.impl.DefaultMutableKrate
import ru.astrainteractive.klibs.kstorage.api.impl.DefaultStateFlowMutableKrate
import ru.astrainteractive.klibs.kstorage.api.provider.DefaultValueFactory
import ru.astrainteractive.klibs.kstorage.suspend.FlowKrate
import ru.astrainteractive.klibs.kstorage.suspend.FlowMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultFlowMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.provider.SuspendValueFactory

object KrateDefaultExt {
    /**
     * convert nullable [Krate] to type-safe via decorating [DefaultMutableKrate]
     */
    fun <T : Any> Krate<T?>.withDefault(factory: DefaultValueFactory<T>): Krate<T> {
        return DefaultMutableKrate(
            factory = factory,
            loader = { loadAndGet() ?: factory.create() }
        )
    }

    /**
     * convert nullable [MutableKrate] to type-safe via decorating [DefaultMutableKrate]
     */
    fun <T : Any> MutableKrate<T?>.withDefault(factory: DefaultValueFactory<T>): MutableKrate<T> {
        return DefaultMutableKrate(
            factory = factory,
            saver = { value -> save(value) },
            loader = { loadAndGet() ?: factory.create() }
        )
    }

    /**
     * convert nullable [StateFlowMutableKrate] to type-safe via decorating [StateFlowMutableKrate]
     */
    fun <T : Any> StateFlowMutableKrate<T?>.withDefault(factory: DefaultValueFactory<T>): StateFlowMutableKrate<T> {
        return DefaultStateFlowMutableKrate(
            factory = factory,
            saver = { value -> save(value) },
            loader = { loadAndGet() ?: factory.create() }
        )
    }

    /**
     * convert nullable [FlowKrate] to type-safe via decorating [DefaultFlowMutableKrate]
     */
    fun <T : Any> FlowKrate<T?>.withDefault(factory: SuspendValueFactory<T>): FlowKrate<T> {
        return DefaultFlowMutableKrate(
            factory = factory,
            loader = { flow.map { it ?: factory.create() } }
        )
    }

    /**
     * convert nullable [FlowMutableKrate] to type-safe via decorating [DefaultFlowMutableKrate]
     */
    fun <T : Any> FlowMutableKrate<T?>.withDefault(factory: SuspendValueFactory<T>): FlowMutableKrate<T> {
        return DefaultFlowMutableKrate(
            factory = factory,
            loader = { flow.map { it ?: factory.create() } },
            saver = { value -> save(value) }
        )
    }
}