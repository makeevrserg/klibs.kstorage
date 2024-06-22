package ru.astrainteractive.klibs.kstorage.util

import ru.astrainteractive.klibs.kstorage.api.Krate
import ru.astrainteractive.klibs.kstorage.api.StateFlowKrate
import ru.astrainteractive.klibs.kstorage.api.impl.DefaultMutableKrate
import ru.astrainteractive.klibs.kstorage.api.impl.DefaultStateFlowMutableKrate
import ru.astrainteractive.klibs.kstorage.api.provider.ValueFactory
import ru.astrainteractive.klibs.kstorage.suspend.FlowKrate
import ru.astrainteractive.klibs.kstorage.suspend.StateFlowSuspendKrate
import ru.astrainteractive.klibs.kstorage.suspend.SuspendKrate
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultFlowMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultSuspendMutableKrate

object KrateDefaultExt {
    /**
     * convert nullable [Krate] to type-safe via decorating [DefaultMutableKrate]
     */
    fun <T : Any> Krate<T?>.withDefault(factory: ValueFactory<T>): Krate<T> {
        return DefaultMutableKrate(
            factory = factory,
            loader = { loadAndGet() }
        )
    }

    /**
     * convert nullable [ Krate.Mutable] to type-safe via decorating [DefaultMutableKrate]
     */
    fun <T : Any> Krate.Mutable<T?>.withDefault(factory: ValueFactory<T>): Krate.Mutable<T> {
        return DefaultMutableKrate(
            factory = factory,
            saver = { value -> save(value) },
            loader = { loadAndGet() }
        )
    }

    /**
     * convert nullable [StateFlowKrate] to type-safe via decorating [DefaultStateFlowMutableKrate]
     */
    fun <T : Any> StateFlowKrate<T?>.withDefault(factory: ValueFactory<T>): StateFlowKrate<T> {
        return DefaultStateFlowMutableKrate(
            factory = factory,
            loader = { loadAndGet() }
        )
    }

    /**
     * convert nullable [StateFlowKrate.Mutable] to type-safe via decorating [DefaultStateFlowMutableKrate]
     */
    fun <T : Any> StateFlowKrate.Mutable<T?>.withDefault(factory: ValueFactory<T>): StateFlowKrate.Mutable<T> {
        return DefaultStateFlowMutableKrate(
            factory = factory,
            saver = { value -> save(value) },
            loader = { loadAndGet() }
        )
    }

    /**
     * convert nullable [FlowKrate] to type-safe via decorating [DefaultSuspendMutableKrate]
     */
    fun <T : Any> SuspendKrate<T?>.withDefault(factory: ValueFactory<T>): SuspendKrate<T> {
        return DefaultSuspendMutableKrate(
            factory = factory,
            loader = { loadAndGet() }
        )
    }

    /**
     * convert nullable [SuspendKrate.Mutable] to type-safe via decorating [DefaultSuspendMutableKrate]
     */
    fun <T : Any> SuspendKrate.Mutable<T?>.withDefault(factory: ValueFactory<T>): SuspendKrate.Mutable<T> {
        return DefaultSuspendMutableKrate(
            factory = factory,
            loader = { loadAndGet() },
            saver = { value -> save(value) }
        )
    }

    /**
     * convert nullable [StateFlowSuspendKrate.Mutable] to type-safe via decorating [DefaultSuspendMutableKrate]
     */
    fun <T : Any> StateFlowSuspendKrate.Mutable<T?>.withDefault(
        factory: ValueFactory<T>
    ): StateFlowSuspendKrate.Mutable<T> {
        return DefaultSuspendMutableKrate(
            factory = factory,
            loader = { loadAndGet() },
            saver = { value -> save(value) }
        )
    }

    /**
     * convert nullable [FlowKrate] to type-safe via decorating [DefaultFlowMutableKrate]
     */
    fun <T : Any> FlowKrate<T?>.withDefault(factory: ValueFactory<T>): FlowKrate<T> {
        return DefaultFlowMutableKrate(
            factory = factory,
            loader = { flow }
        )
    }

    /**
     * convert nullable [FlowKrate.Mutable] to type-safe via decorating [DefaultFlowMutableKrate]
     */
    fun <T : Any> FlowKrate.Mutable<T?>.withDefault(factory: ValueFactory<T>): FlowKrate.Mutable<T> {
        return DefaultFlowMutableKrate(
            factory = factory,
            loader = { flow },
            saver = { value -> save(value) }
        )
    }
}
