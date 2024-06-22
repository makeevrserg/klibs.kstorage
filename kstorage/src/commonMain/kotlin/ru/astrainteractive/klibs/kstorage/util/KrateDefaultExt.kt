package ru.astrainteractive.klibs.kstorage.util

import ru.astrainteractive.klibs.kstorage.api.Krate
import ru.astrainteractive.klibs.kstorage.api.MutableKrate
import ru.astrainteractive.klibs.kstorage.api.StateFlowKrate
import ru.astrainteractive.klibs.kstorage.api.StateFlowMutableKrate
import ru.astrainteractive.klibs.kstorage.api.impl.DefaultMutableKrate
import ru.astrainteractive.klibs.kstorage.api.impl.DefaultStateFlowMutableKrate
import ru.astrainteractive.klibs.kstorage.api.provider.ValueFactory
import ru.astrainteractive.klibs.kstorage.suspend.FlowKrate
import ru.astrainteractive.klibs.kstorage.suspend.FlowMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.StateFlowSuspendMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.SuspendKrate
import ru.astrainteractive.klibs.kstorage.suspend.SuspendMutableKrate
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
     * convert nullable [MutableKrate] to type-safe via decorating [DefaultMutableKrate]
     */
    fun <T : Any> MutableKrate<T?>.withDefault(factory: ValueFactory<T>): MutableKrate<T> {
        return DefaultMutableKrate(
            factory = factory,
            saver = { value -> save(value) },
            loader = { loadAndGet() }
        )
    }

    /**
     * convert nullable [StateFlowKrate] to type-safe via decorating [StateFlowMutableKrate]
     */
    fun <T : Any> StateFlowKrate<T?>.withDefault(factory: ValueFactory<T>): StateFlowKrate<T> {
        return DefaultStateFlowMutableKrate(
            factory = factory,
            loader = { loadAndGet() }
        )
    }

    /**
     * convert nullable [StateFlowMutableKrate] to type-safe via decorating [StateFlowMutableKrate]
     */
    fun <T : Any> StateFlowMutableKrate<T?>.withDefault(factory: ValueFactory<T>): StateFlowMutableKrate<T> {
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
     * convert nullable [SuspendMutableKrate] to type-safe via decorating [DefaultSuspendMutableKrate]
     */
    fun <T : Any> SuspendMutableKrate<T?>.withDefault(factory: ValueFactory<T>): SuspendMutableKrate<T> {
        return DefaultSuspendMutableKrate(
            factory = factory,
            loader = { loadAndGet() },
            saver = { value -> save(value) }
        )
    }

    /**
     * convert nullable [SuspendMutableKrate] to type-safe via decorating [DefaultSuspendMutableKrate]
     */
    fun <T : Any> StateFlowSuspendMutableKrate<T?>.withDefault(
        factory: ValueFactory<T>
    ): StateFlowSuspendMutableKrate<T> {
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
     * convert nullable [FlowMutableKrate] to type-safe via decorating [DefaultFlowMutableKrate]
     */
    fun <T : Any> FlowMutableKrate<T?>.withDefault(factory: ValueFactory<T>): FlowMutableKrate<T> {
        return DefaultFlowMutableKrate(
            factory = factory,
            loader = { flow },
            saver = { value -> save(value) }
        )
    }
}
