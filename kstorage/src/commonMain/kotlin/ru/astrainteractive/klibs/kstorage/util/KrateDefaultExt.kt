package ru.astrainteractive.klibs.kstorage.util

import ru.astrainteractive.klibs.kstorage.api.Krate
import ru.astrainteractive.klibs.kstorage.api.MutableKrate
import ru.astrainteractive.klibs.kstorage.api.flow.StateFlowKrate
import ru.astrainteractive.klibs.kstorage.api.flow.StateFlowMutableKrate
import ru.astrainteractive.klibs.kstorage.api.impl.DefaultMutableKrate
import ru.astrainteractive.klibs.kstorage.api.impl.DefaultStateFlowMutableKrate
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import ru.astrainteractive.klibs.kstorage.coroutines.getIoDispatcher
import ru.astrainteractive.klibs.kstorage.suspend.SuspendKrate
import ru.astrainteractive.klibs.kstorage.suspend.SuspendMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.flow.FlowKrate
import ru.astrainteractive.klibs.kstorage.suspend.flow.FlowMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.flow.StateFlowSuspendMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultFlowMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultSuspendMutableKrate
import kotlin.coroutines.CoroutineContext

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
    fun <T : Any> MutableKrate<T?>.withDefault(factory: ValueFactory<T>): MutableKrate<T> {
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
     * convert nullable [StateFlowMutableKrate] to type-safe via decorating [DefaultStateFlowMutableKrate]
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
    fun <T : Any> SuspendKrate<T?>.withDefault(
        factory: ValueFactory<T>,
        coroutineContext: CoroutineContext = getIoDispatcher()
    ): SuspendKrate<T> {
        return DefaultSuspendMutableKrate(
            factory = factory,
            loader = { loadAndGet() },
            coroutineContext = coroutineContext
        )
    }

    /**
     * convert nullable [SuspendMutableKrate] to type-safe via decorating [DefaultSuspendMutableKrate]
     */
    fun <T : Any> SuspendMutableKrate<T?>.withDefault(
        factory: ValueFactory<T>,
        coroutineContext: CoroutineContext = getIoDispatcher()
    ): SuspendMutableKrate<T> {
        return DefaultSuspendMutableKrate(
            factory = factory,
            loader = { loadAndGet() },
            saver = { value -> save(value) },
            coroutineContext = coroutineContext
        )
    }

    /**
     * convert nullable [StateFlowSuspendMutableKrate] to type-safe via decorating [DefaultSuspendMutableKrate]
     */
    fun <T : Any> StateFlowSuspendMutableKrate<T?>.withDefault(
        factory: ValueFactory<T>,
        coroutineContext: CoroutineContext = getIoDispatcher()
    ): StateFlowSuspendMutableKrate<T> {
        return DefaultSuspendMutableKrate(
            factory = factory,
            loader = { loadAndGet() },
            saver = { value -> save(value) },
            coroutineContext = coroutineContext
        )
    }

    /**
     * convert nullable [FlowKrate] to type-safe via decorating [DefaultFlowMutableKrate]
     */
    fun <T : Any> FlowKrate<T?>.withDefault(
        factory: ValueFactory<T>,
        coroutineContext: CoroutineContext = getIoDispatcher()
    ): FlowKrate<T> {
        return DefaultFlowMutableKrate(
            factory = factory,
            loader = { flow },
            coroutineContext = coroutineContext
        )
    }

    /**
     * convert nullable [FlowMutableKrate] to type-safe via decorating [DefaultFlowMutableKrate]
     */
    fun <T : Any> FlowMutableKrate<T?>.withDefault(
        factory: ValueFactory<T>,
        coroutineContext: CoroutineContext
    ): FlowMutableKrate<T> {
        return DefaultFlowMutableKrate(
            factory = factory,
            loader = { flow },
            saver = { value -> save(value) },
            coroutineContext = coroutineContext
        )
    }
}
