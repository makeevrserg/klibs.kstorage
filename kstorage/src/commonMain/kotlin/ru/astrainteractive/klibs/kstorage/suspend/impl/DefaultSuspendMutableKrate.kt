package ru.astrainteractive.klibs.kstorage.suspend.impl

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import ru.astrainteractive.klibs.kstorage.coroutines.getIoDispatcher
import ru.astrainteractive.klibs.kstorage.suspend.flow.StateFlowSuspendMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.value.SuspendValueLoader
import ru.astrainteractive.klibs.kstorage.suspend.value.SuspendValueSaver
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class DefaultSuspendMutableKrate<T>(
    private val factory: ValueFactory<T>,
    private val loader: SuspendValueLoader<T>,
    private val saver: SuspendValueSaver<T> = SuspendValueSaver.Empty(),
    coroutineContext: CoroutineContext = getIoDispatcher()
) : StateFlowSuspendMutableKrate<T> {
    private val _cachedStateFlow = MutableStateFlow(factory.create())
    override val cachedStateFlow: StateFlow<T> = _cachedStateFlow.asStateFlow()

    override suspend fun loadAndGet(): T {
        val value = loader.loadAndGet() ?: factory.create()
        _cachedStateFlow.update { value }
        return value
    }

    override suspend fun save(value: T) {
        saver.save(value)
        _cachedStateFlow.update { value }
    }

    override suspend fun reset() {
        val default = factory.create()
        save(default)
    }

    init {
        val scope = CoroutineScope(EmptyCoroutineContext)
        scope.launch(coroutineContext) {
            loadAndGet()
        }.invokeOnCompletion { scope.cancel() }
    }
}
