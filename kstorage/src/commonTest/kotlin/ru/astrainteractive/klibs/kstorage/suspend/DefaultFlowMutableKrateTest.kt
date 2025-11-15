package ru.astrainteractive.klibs.kstorage.suspend

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultFlowMutableKrate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultFlowMutableKrateTest {

    @Test
    fun GIVEN_krate_without_any_value_WHEN_first_THEN_null() = runTest {
        val krate = DefaultFlowMutableKrate<Int?>(
            factory = { null },
            loader = { flowOf() },
        )
        assertNull(krate.flow.firstOrNull())
        assertNull(krate.getValue())
        assertNull(krate.stateFlow(backgroundScope).first())
    }

    @Test
    fun GIVEN_krate_with_null_factory_nonnull_value_WHEN_get_THEN_non_null() = runTest {
        val krate = DefaultFlowMutableKrate(
            factory = { null },
            loader = { flowOf(1) },
        )
        assertEquals(1, krate.flow.filterNotNull().first())
        assertEquals(1, krate.getValue())
        assertEquals(1, krate.stateFlow(backgroundScope).first())
    }

    @Test
    fun GIVEN_krate_with_factory_null_loader_WHEN_get_THEN_from_factory() = runTest {
        val krate = DefaultFlowMutableKrate(
            factory = { 1 },
            loader = { flowOf() },
        )
        assertNull(krate.flow.filterNotNull().firstOrNull())
        assertEquals(1, krate.getValue())
        assertEquals(1, krate.stateFlow(backgroundScope).first())
    }

    @Test
    fun GIVEN_krate_with_recreated_flow_WHEN_getvalue_THEN_same() = runTest {
        val krate = DefaultFlowMutableKrate(
            factory = { 1 },
            loader = { flowOf() },
            saver = {}
        )
        assertNull(krate.flow.filterNotNull().firstOrNull())
        assertEquals(1, krate.getValue())
        assertEquals(1, krate.stateFlow(backgroundScope).first())
    }

    @Test
    fun GIVEN_krate_with_replay_flow_WHEN_save_THEN_saved() = runTest {
        val stateFlow = MutableStateFlow(1)
        val krate = DefaultFlowMutableKrate(
            factory = { 1 },
            loader = { stateFlow },
            saver = { stateFlow.emit(it) }
        )
        assertEquals(1, krate.getValue())
        assertEquals(1, krate.flow.first())
        krate.save(2)
        assertEquals(2, krate.getValue())
        assertEquals(2, krate.flow.first())
    }
}
